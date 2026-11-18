package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.mail.application.port.secondary.ShipmentNotificationPolicyPort;
import com.warehouse.mail.domain.vo.ShipmentNotificationPolicy;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperatorShipmentNotificationPolicyAdapter implements ShipmentNotificationPolicyPort {

    private static final Logger log = LoggerFactory.getLogger(OperatorShipmentNotificationPolicyAdapter.class);

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    public OperatorShipmentNotificationPolicyAdapter(final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public ShipmentNotificationPolicy policyFor(final OperatorId operatorId) {
        if (operatorId == null) {
            log.debug("Shipment event carries no operator, recipient e-mails are not restricted");
            return ShipmentNotificationPolicy.unrestricted();
        }
        final ShipmentNotificationConfigurationDto configuration =
                operatorConfigurationApiService.getShipmentNotificationConfiguration(operatorId);
        return new ShipmentNotificationPolicy(
                configuration.shouldSendEmailToRecipient(configuration.notifyRecipientOnCreated()),
                configuration.shouldSendEmailToRecipient(configuration.notifyRecipientOnDispatched()),
                configuration.shouldSendEmailToRecipient(configuration.notifyRecipientOnDelivered()));
    }
}
