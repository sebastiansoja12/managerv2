package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPort;
import com.warehouse.organisationstructure.department.domain.event.DepartmentDeleted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
public class DepartmentDeletedEventListener {

    private final DeliveryAreaPort deliveryAreaPort;

    public DepartmentDeletedEventListener(final DeliveryAreaPort deliveryAreaPort) {
        this.deliveryAreaPort = deliveryAreaPort;
    }

    @EventListener
    public void handle(final DepartmentDeleted event) {
        try {
            deliveryAreaPort.reassignAfterDepartmentDeleted(event.getSnapshot().departmentId());
        } catch (final RuntimeException exception) {
            log.warn("Delivery areas were not reassigned after department {} was deleted: {}",
                    event.getSnapshot().departmentCode().getValue(), exception.getMessage());
        }
    }
}
