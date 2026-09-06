package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPort;
import com.warehouse.organisationstructure.subscription.application.port.primary.command.AssignStarterSubscriptionCommand;
import org.springframework.stereotype.Component;

@Component
public class OperatorSubscriptionKafkaEventListener {

    private final OperatorSubscriptionPort operatorSubscriptionPort;

    public OperatorSubscriptionKafkaEventListener(final OperatorSubscriptionPort operatorSubscriptionPort) {
        this.operatorSubscriptionPort = operatorSubscriptionPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.operator-configuration-create:operator.configuration.create}",
            groupId = "${manager.kafka.consumer-groups.operator-subscription-create:manager-operator-subscription-create}"
    )
    public void handle(final OperatorCreatedIntegrationEvent event) {
        operatorSubscriptionPort.assignStarter(new AssignStarterSubscriptionCommand(event.operatorId(),
                event.timestamp()));
    }
}
