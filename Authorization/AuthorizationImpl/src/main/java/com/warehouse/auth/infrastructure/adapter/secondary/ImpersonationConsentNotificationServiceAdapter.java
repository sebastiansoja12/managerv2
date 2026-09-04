package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.ImpersonationConsentNotificationServicePort;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.api.ImpersonationConsentChangedNotificationDto;
import com.warehouse.commonassets.identificator.OperatorId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ImpersonationConsentNotificationServiceAdapter implements ImpersonationConsentNotificationServicePort {

    private static final String CONSENT_DESTINATION = "/queue/impersonation-consents";

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    public ImpersonationConsentNotificationServiceAdapter(final SimpMessagingTemplate messagingTemplate,
                                                          final UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public void notifyConsentsChanged(final OperatorId operatorId) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send(operatorId);
                }
            });
            return;
        }
        send(operatorId);
    }

    private void send(final OperatorId operatorId) {
        final ImpersonationConsentChangedNotificationDto payload =
                new ImpersonationConsentChangedNotificationDto(operatorId.value());
        userRepository.findActiveSuperadminIds(operatorId)
                .forEach(userId -> messagingTemplate.convertAndSendToUser(String.valueOf(userId.value()),
                        CONSENT_DESTINATION, payload));
    }
}
