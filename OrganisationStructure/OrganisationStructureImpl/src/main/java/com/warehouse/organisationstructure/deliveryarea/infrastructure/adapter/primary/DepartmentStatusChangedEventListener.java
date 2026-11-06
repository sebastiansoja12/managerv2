package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPort;
import com.warehouse.organisationstructure.department.domain.event.DepartmentActivated;
import com.warehouse.organisationstructure.department.domain.event.DepartmentArchived;
import com.warehouse.organisationstructure.department.domain.event.DepartmentChanged;
import com.warehouse.organisationstructure.department.domain.event.DepartmentDeactivated;
import com.warehouse.organisationstructure.department.domain.event.DepartmentSuspended;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
public class DepartmentStatusChangedEventListener {

    private final DeliveryAreaPort deliveryAreaPort;

    public DepartmentStatusChangedEventListener(final DeliveryAreaPort deliveryAreaPort) {
        this.deliveryAreaPort = deliveryAreaPort;
    }

    @EventListener
    public void handle(final DepartmentActivated event) {
        recalculate(event);
    }

    @EventListener
    public void handle(final DepartmentDeactivated event) {
        recalculate(event);
    }

    @EventListener
    public void handle(final DepartmentSuspended event) {
        recalculate(event);
    }

    @EventListener
    public void handle(final DepartmentArchived event) {
        recalculate(event);
    }

    private void recalculate(final DepartmentChanged event) {
        try {
            deliveryAreaPort.recalculateAfterDepartmentStatusChanged(event.getSnapshot().departmentId());
        } catch (final RuntimeException exception) {
            log.warn("Delivery areas were not recalculated after status of department {} changed: {}",
                    event.getSnapshot().departmentCode().getValue(), exception.getMessage());
        }
    }
}
