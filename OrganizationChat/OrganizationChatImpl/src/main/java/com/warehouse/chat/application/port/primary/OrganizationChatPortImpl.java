package com.warehouse.chat.application.port.primary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.application.port.primary.command.SendChatMessageCommand;
import com.warehouse.chat.application.port.primary.result.OpenChatConversationResult;
import com.warehouse.chat.application.port.secondary.ChatPresenceNotificationServicePort;
import com.warehouse.chat.application.port.secondary.ChatPresenceRepository;
import com.warehouse.chat.application.port.secondary.ChatUserServicePort;
import com.warehouse.chat.application.port.secondary.OrganizationChatRepository;
import com.warehouse.chat.domain.event.OrganizationChatMessageCreatedEvent;
import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.ChatPresence;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static com.warehouse.chat.domain.exception.ChatException.Reason.NOT_FOUND;

@Transactional
public class OrganizationChatPortImpl implements OrganizationChatPort {

    private final OrganizationChatRepository repository;
    private final ChatUserServicePort userServicePort;
    private final DomainEventPublisher eventPublisher;
    private final Clock clock;
    private final ChatPresenceRepository presenceRepository;
    private final ChatPresenceNotificationServicePort presenceNotificationServicePort;

    public OrganizationChatPortImpl(final OrganizationChatRepository repository,
                                   final ChatUserServicePort userServicePort,
                                   final DomainEventPublisher eventPublisher, final Clock clock,
                                   final ChatPresenceRepository presenceRepository,
                                   final ChatPresenceNotificationServicePort presenceNotificationServicePort) {
        this.repository = repository;
        this.userServicePort = userServicePort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
        this.presenceRepository = presenceRepository;
        this.presenceNotificationServicePort = presenceNotificationServicePort;
    }

    @Override
    public void connectPresence(final String sessionId, final UserId userId) {
        final OperatorId operatorId = userServicePort.findRecipient(userId)
                .orElseThrow(() -> new ChatException(NOT_FOUND, "Chat user was not found"))
                .operatorId();
        presenceRepository.connect(new ChatPresence(sessionId, userId, operatorId));
        notifyPresence(operatorId);
    }

    @Override
    public void disconnectPresence(final String sessionId) {
        presenceRepository.disconnect(sessionId)
                .ifPresent(presence -> notifyPresence(presence.operatorId()));
    }

    private void notifyPresence(final OperatorId operatorId) {
        final List<UserId> onlineUsers = presenceRepository.findOnlineUsers(operatorId);
        presenceNotificationServicePort.notifyUsers(onlineUsers, onlineUsers);
    }

    @Override
    public OpenChatConversationResult openDirectConversation(final UserId participantUserId) {
        final UserId currentUserId = userServicePort.currentUserId();
        final OperatorId operatorId = userServicePort.currentOperatorId();
        final DirectChatParticipants participants = new DirectChatParticipants(currentUserId, participantUserId);
        userServicePort.findRecipient(participantUserId)
                .orElseThrow(() -> new ChatException(NOT_FOUND, "Chat recipient was not found or is inactive"))
                .requireAvailableTo(operatorId);
        final ChatConversation conversation = repository.findDirectConversation(participants)
                .orElseGet(() -> createConversation(operatorId, participants));
        return new OpenChatConversationResult(conversation, currentUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageSnapshot> findMessages(final ChatConversationId conversationId,
                                                final ChatMessageId beforeMessageId, final int limit) {
        requireAccessibleConversation(conversationId, userServicePort.currentUserId());
        return repository.findMessages(conversationId, beforeMessageId, Math.clamp(limit, 1, 100));
    }

    @Override
    public ChatMessageSnapshot sendMessage(final ChatConversationId conversationId, final SendChatMessageCommand command) {
        final UserId currentUserId = userServicePort.currentUserId();
        final ChatConversation conversation = requireAccessibleConversation(conversationId, currentUserId);
        return repository.findMessage(currentUserId, command.clientMessageId())
                .map(conversation::reuseMessage)
                .orElseGet(() -> sendNewMessage(conversation, currentUserId, command));
    }

    private ChatMessageSnapshot sendNewMessage(final ChatConversation conversation, final UserId currentUserId,
                                              final SendChatMessageCommand command) {
        final Instant now = clock.instant();
        final ChatMessageSnapshot message = conversation.sendMessage(currentUserId, command.clientMessageId(),
                command.body(), now);
        repository.saveMessage(message);
        repository.saveConversation(conversation);
        eventPublisher.publish(new OrganizationChatMessageCreatedEvent(message, conversation.participants(), now));
        return message;
    }

    private ChatConversation createConversation(final OperatorId operatorId, final DirectChatParticipants participants) {
        final ChatConversation conversation = ChatConversation.open(operatorId, participants, clock.instant());
        repository.createConversation(conversation);
        return conversation;
    }

    private ChatConversation requireAccessibleConversation(final ChatConversationId conversationId, final UserId userId) {
        final ChatConversation conversation = repository.findConversation(conversationId)
                .orElseThrow(() -> new ChatException(NOT_FOUND, "Chat conversation was not found"));
        conversation.requireParticipant(userId);
        return conversation;
    }
}
