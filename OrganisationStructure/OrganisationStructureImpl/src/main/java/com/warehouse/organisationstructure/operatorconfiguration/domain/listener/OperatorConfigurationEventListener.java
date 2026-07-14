package com.warehouse.organisationstructure.operatorconfiguration.domain.listener;

import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OperatorConfigurationEventListener {

    private final OperatorConfigurationService operatorConfigurationService;

    public OperatorConfigurationEventListener(final OperatorConfigurationService operatorConfigurationService) {
        this.operatorConfigurationService = operatorConfigurationService;
    }

    @EventListener
    public void handle(final OperatorCreatedEvent event) {
        final OperatorSnapshot snapshot = event.getSnapshot();
        operatorConfigurationService.create(snapshot.operatorId(), snapshot.configuration());
    }
}
