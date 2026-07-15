package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.application.port.secondary.ChatNotificationServicePort;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.chat.infrastructure.adapter.secondary.api.ChatMessageNotificationDto;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class ChatNotificationServiceAdapter implements ChatNotificationServicePort {

    private static final String MESSAGE_DESTINATION = "/queue/chat/messages";

    private final SimpMessagingTemplate messagingTemplate;

    public ChatNotificationServiceAdapter(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyParticipants(final ChatMessageSnapshot message, final DirectChatParticipants participants) {
        final ChatMessageNotificationDto.Message payload = new ChatMessageNotificationDto.Message(message.id(),
                message.conversationId(), message.senderUserId(), message.clientMessageId(), message.body(), message.sentAt());
        notifyUser(participants.firstUserId(), participants.secondUserId(), payload);
        notifyUser(participants.secondUserId(), participants.firstUserId(), payload);
    }

    private void notifyUser(final UserId userId, final UserId participantUserId,
                            final ChatMessageNotificationDto.Message message) {
        messagingTemplate.convertAndSendToUser(String.valueOf(userId.value()), MESSAGE_DESTINATION,
                new ChatMessageNotificationDto(participantUserId, message));
    }
}
