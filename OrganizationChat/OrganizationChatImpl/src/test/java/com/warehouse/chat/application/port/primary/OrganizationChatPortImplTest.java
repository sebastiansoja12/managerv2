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
import com.warehouse.chat.domain.vo.ChatRecipient;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static com.warehouse.chat.ChatFixture.*;
import static com.warehouse.chat.domain.exception.ChatException.Reason.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationChatPortImplTest {

    @Mock
    private OrganizationChatRepository repository;
    @Mock
    private ChatUserServicePort userServicePort;
    @Mock
    private DomainEventPublisher eventPublisher;

    private OrganizationChatPortImpl chatPort;
    @Mock
    private ChatPresenceRepository presenceRepository;
    @Mock
    private ChatPresenceNotificationServicePort presenceNotificationServicePort;

    @BeforeEach
    void setUp() {
        chatPort = new OrganizationChatPortImpl(repository, userServicePort, eventPublisher,
                Clock.fixed(NOW, ZoneOffset.UTC), presenceRepository, presenceNotificationServicePort);
        lenient().when(userServicePort.currentUserId()).thenReturn(CURRENT_USER_ID);
        lenient().when(userServicePort.currentOperatorId()).thenReturn(OPERATOR_ID);
    }

    @Test
    void shouldReturnExistingConversationWithoutPersistingIt() {
        final ChatConversation conversation = conversation();
        when(userServicePort.findRecipient(PARTICIPANT_USER_ID)).thenReturn(Optional.of(new ChatRecipient(OPERATOR_ID, true)));
        when(repository.findDirectConversation(PARTICIPANTS)).thenReturn(Optional.of(conversation));

        final OpenChatConversationResult result = chatPort.openDirectConversation(PARTICIPANT_USER_ID);

        assertThat(result.conversation()).isSameAs(conversation);
        assertThat(result.currentUserId()).isEqualTo(CURRENT_USER_ID);
        verify(repository, never()).createConversation(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldCreateConversationThroughRepositoryPort() {
        when(userServicePort.findRecipient(PARTICIPANT_USER_ID)).thenReturn(Optional.of(new ChatRecipient(OPERATOR_ID, true)));

        final OpenChatConversationResult result = chatPort.openDirectConversation(PARTICIPANT_USER_ID);

        verify(repository).createConversation(result.conversation());
        assertThat(result.conversation().id().value()).isPositive();
        assertThat(result.conversation().participants()).isEqualTo(PARTICIPANTS);
        assertThat(result.conversation().createdAt()).isEqualTo(NOW);
    }

    @Test
    void shouldRejectMissingRecipientBeforePersistence() {
        assertThatThrownBy(() -> chatPort.openDirectConversation(PARTICIPANT_USER_ID))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", NOT_FOUND);

        verifyNoInteractions(repository, eventPublisher);
    }

    @Test
    void shouldRejectForeignRecipientBeforePersistence() {
        when(userServicePort.findRecipient(PARTICIPANT_USER_ID))
                .thenReturn(Optional.of(new ChatRecipient(com.warehouse.commonassets.identificator.OperatorId.of(99L), true)));

        assertThatThrownBy(() -> chatPort.openDirectConversation(PARTICIPANT_USER_ID))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);

        verifyNoInteractions(repository, eventPublisher);
    }

    @Test
    void shouldPersistMessageAndConversationBeforePublishingSnapshot() {
        final ChatConversation conversation = accessibleConversation();

        final ChatMessageSnapshot result = chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "  Ready  "));

        final InOrder order = inOrder(repository, eventPublisher);
        order.verify(repository).saveMessage(result);
        order.verify(repository).saveConversation(conversation);
        final ArgumentCaptor<OrganizationChatMessageCreatedEvent> event = ArgumentCaptor.forClass(OrganizationChatMessageCreatedEvent.class);
        order.verify(eventPublisher).publish(event.capture());
        assertThat(event.getValue().message()).isEqualTo(result);
        assertThat(event.getValue().participants()).isEqualTo(PARTICIPANTS);
        assertThat(event.getValue().getTimestamp()).isEqualTo(NOW);
        assertThat(result.body()).isEqualTo("Ready");
    }

    @Test
    void shouldReturnRetriedMessageWithoutSavingOrPublishingAgain() {
        final ChatConversation conversation = accessibleConversation();
        final ChatMessageSnapshot original = message(10L);
        when(repository.findMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID)).thenReturn(Optional.of(original));

        final ChatMessageSnapshot result = chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Retry"));

        assertThat(result).isSameAs(original);
        assertThat(conversation.lastMessageAt()).isNull();
        verify(repository, never()).saveMessage(any());
        verify(repository, never()).saveConversation(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldRejectClientIdentifierUsedInAnotherConversation() {
        accessibleConversation();
        when(repository.findMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID)).thenReturn(Optional.of(new ChatMessageSnapshot(
                ChatMessageId.of(10L), ChatConversationId.of(99L), CURRENT_USER_ID, CLIENT_MESSAGE_ID, "Original", NOW)));

        assertThatThrownBy(() -> chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready")))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", CONFLICT);

        verify(repository, never()).saveMessage(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldNotPublishWhenMessagePersistenceFails() {
        accessibleConversation();
        doThrow(new IllegalStateException("Database unavailable")).when(repository).saveMessage(any());

        assertThatThrownBy(() -> chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready")))
                .isInstanceOf(IllegalStateException.class);

        verify(repository, never()).saveConversation(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldNotPublishWhenConversationPersistenceFails() {
        accessibleConversation();
        doThrow(new IllegalStateException("Database unavailable")).when(repository).saveConversation(any());

        assertThatThrownBy(() -> chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready")))
                .isInstanceOf(IllegalStateException.class);

        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldRejectNonParticipantBeforeLoadingMessages() {
        accessibleConversation();
        when(userServicePort.currentUserId()).thenReturn(new UserId(3L));

        assertThatThrownBy(() -> chatPort.findMessages(CONVERSATION_ID, null, 50))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);

        verify(repository, never()).findMessages(any(), any(), anyInt());
    }

    @Test
    void shouldRejectNonParticipantBeforeLookingUpRetriedMessage() {
        accessibleConversation();
        when(userServicePort.currentUserId()).thenReturn(new UserId(3L));

        assertThatThrownBy(() -> chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready")))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);

        verify(repository, never()).findMessage(any(), any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldScopeConversationLookupToCurrentOrganization() {
        assertThatThrownBy(() -> chatPort.findMessages(CONVERSATION_ID, null, 50))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", NOT_FOUND);

        verify(repository).findConversation(CONVERSATION_ID);
        verify(repository, never()).findMessages(any(), any(), anyInt());
    }

    @Test
    void shouldBoundHistoryLimitAndForwardCursor() {
        accessibleConversation();
        final ChatMessageId cursor = ChatMessageId.of(20L);
        final List<ChatMessageSnapshot> messages = List.of(message(10L));
        when(repository.findMessages(CONVERSATION_ID, cursor, 100)).thenReturn(messages);

        assertThat(chatPort.findMessages(CONVERSATION_ID, cursor, 999)).isEqualTo(messages);
        chatPort.findMessages(CONVERSATION_ID, null, 0);

        verify(repository).findMessages(CONVERSATION_ID, null, 1);
    }

    @Test
    void shouldConnectPresenceAndNotifyOnlineUsersInTheOrganization() {
        when(userServicePort.findRecipient(CURRENT_USER_ID)).thenReturn(Optional.of(new ChatRecipient(OPERATOR_ID, true)));
        when(presenceRepository.findOnlineUsers(OPERATOR_ID)).thenReturn(List.of(CURRENT_USER_ID, PARTICIPANT_USER_ID));

        chatPort.connectPresence("session-1", CURRENT_USER_ID);

        verify(presenceRepository).connect(new ChatPresence("session-1", CURRENT_USER_ID, OPERATOR_ID));
        verify(presenceNotificationServicePort).notifyUsers(
                List.of(CURRENT_USER_ID, PARTICIPANT_USER_ID), List.of(CURRENT_USER_ID, PARTICIPANT_USER_ID));
    }

    @Test
    void shouldDisconnectPresenceAndNotifyRemainingOnlineUsers() {
        final ChatPresence disconnectedPresence = new ChatPresence("session-1", CURRENT_USER_ID, OPERATOR_ID);
        when(presenceRepository.disconnect("session-1")).thenReturn(Optional.of(disconnectedPresence));
        when(presenceRepository.findOnlineUsers(OPERATOR_ID)).thenReturn(List.of(PARTICIPANT_USER_ID));

        chatPort.disconnectPresence("session-1");

        verify(presenceNotificationServicePort).notifyUsers(
                List.of(PARTICIPANT_USER_ID), List.of(PARTICIPANT_USER_ID));
    }

    private ChatConversation accessibleConversation() {
        final ChatConversation conversation = conversation();
        when(repository.findConversation(CONVERSATION_ID)).thenReturn(Optional.of(conversation));
        return conversation;
    }
}
