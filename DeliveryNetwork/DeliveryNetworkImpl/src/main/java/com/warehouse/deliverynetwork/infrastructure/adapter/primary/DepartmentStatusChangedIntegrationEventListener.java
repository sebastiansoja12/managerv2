package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.department.api.event.DepartmentStatusChangedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentStatusChangedIntegrationEventListener {

    private final DeliveryNetworkPort deliveryNetworkPort;

    public DepartmentStatusChangedIntegrationEventListener(final DeliveryNetworkPort deliveryNetworkPort) {
        this.deliveryNetworkPort = deliveryNetworkPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.department-status-events:department.status.events}",
            groupId = "${manager.kafka.consumer-groups.delivery-network-department-status:manager-delivery-network-department-status}"
    )
    public void handle(final DepartmentStatusChangedIntegrationEvent event) {
        if (event.status() == DepartmentStatusDto.ARCHIVED
                || event.status() == DepartmentStatusDto.DELETED) {
            this.deliveryNetworkPort.removeDepartmentConnections(
                    new DepartmentId(event.affectedDepartmentId().value()));
        }
        log.info("Deleted delivery connections for departmentId: {}", event.affectedDepartmentId().value());
    }
}
