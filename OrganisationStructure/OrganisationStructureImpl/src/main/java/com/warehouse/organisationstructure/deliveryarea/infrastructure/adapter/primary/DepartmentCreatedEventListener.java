package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPort;
import com.warehouse.organisationstructure.department.domain.event.DepartmentCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
public class DepartmentCreatedEventListener {

    private final DeliveryAreaPort deliveryAreaPort;

    public DepartmentCreatedEventListener(final DeliveryAreaPort deliveryAreaPort) {
        this.deliveryAreaPort = deliveryAreaPort;
    }

    @EventListener
    public void handle(final DepartmentCreated event) {
        try {
            deliveryAreaPort.assignAutomaticallyAfterDepartmentCreated(event.getSnapshot().departmentId());
        } catch (final RuntimeException exception) {
            log.warn("Automatic delivery areas were not assigned after department {} was created: {}",
                    event.getSnapshot().departmentCode().getValue(), exception.getMessage());
        }
    }
}
