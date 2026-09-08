package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.application.port.secondary.ChatPresenceNotificationServicePort;
import com.warehouse.chat.infrastructure.adapter.secondary.api.ChatPresenceNotificationDto;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class ChatPresenceNotificationServiceAdapter implements ChatPresenceNotificationServicePort {

    private static final String PRESENCE_DESTINATION = "/queue/chat/presence";

    private final SimpMessagingTemplate messagingTemplate;

    public ChatPresenceNotificationServiceAdapter(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyUsers(final List<UserId> recipients, final List<UserId> onlineUsers) {
        final ChatPresenceNotificationDto notification = new ChatPresenceNotificationDto(
                onlineUsers.stream().map(userId -> String.valueOf(userId.value())).toList()
        );
        recipients.forEach(recipient -> messagingTemplate.convertAndSendToUser(
                String.valueOf(recipient.value()), PRESENCE_DESTINATION, notification
        ));
    }
}
