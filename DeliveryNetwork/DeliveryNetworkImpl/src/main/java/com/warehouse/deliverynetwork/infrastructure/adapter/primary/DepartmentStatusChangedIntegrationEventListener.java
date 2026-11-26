package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.organisationstructure.department.api.dto.DepartmentStatusDto;
import com.warehouse.organisationstructure.department.api.event.DepartmentStatusChangedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentStatusChangedIntegrationEventListener {

    private final DeliveryNetworkPort deliveryNetworkPort;

    public DepartmentStatusChangedIntegrationEventListener(final DeliveryNetworkPort deliveryNetworkPort) {
        this.deliveryNetworkPort = deliveryNetworkPort;
    }

    @IntegrationEventListener(group = "manager-delivery-network-department-status")
    public void handle(final DepartmentStatusChangedIntegrationEvent event) {
        if (event.status() == DepartmentStatusDto.ARCHIVED
                || event.status() == DepartmentStatusDto.DELETED) {
            this.deliveryNetworkPort.removeDepartmentConnections(
                    new DepartmentId(event.affectedDepartmentId().value()));
        }
        log.info("Deleted delivery connections for departmentId: {}", event.affectedDepartmentId().value());
    }
}
