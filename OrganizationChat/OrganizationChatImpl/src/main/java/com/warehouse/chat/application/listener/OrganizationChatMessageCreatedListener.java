package com.warehouse.chat.application.listener;

import com.warehouse.chat.application.port.secondary.ChatNotificationServicePort;
import com.warehouse.chat.domain.event.OrganizationChatMessageCreatedEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public class OrganizationChatMessageCreatedListener {

    private final ChatNotificationServicePort notificationServicePort;

    public OrganizationChatMessageCreatedListener(final ChatNotificationServicePort notificationServicePort) {
        this.notificationServicePort = notificationServicePort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageCreated(final OrganizationChatMessageCreatedEvent event) {
        notificationServicePort.notifyParticipants(event.message(), event.participants());
    }
}
