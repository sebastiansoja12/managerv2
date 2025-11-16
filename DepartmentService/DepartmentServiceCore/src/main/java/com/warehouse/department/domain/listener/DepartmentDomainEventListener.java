package com.warehouse.department.domain.listener;

import com.warehouse.department.domain.event.DepartmentAdminChanged;
import com.warehouse.department.domain.event.DepartmentCreated;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepartmentDomainEventListener {

    private final DepartmentService departmentService;

    public DepartmentDomainEventListener(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @EventListener
    public void handle(final DepartmentCreated event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
        //this.tenantAdminProvisioningPort.createInitialAdminUser(snapshot);
    }

    @EventListener
    public void handle(final DepartmentAdminChanged event) {
        log.info("Department admin changed event: {}", event.getUserId().getValue());
        this.departmentService.changeAdminUser(event.getDepartmentCode(), event.getUserId());
    }
}
