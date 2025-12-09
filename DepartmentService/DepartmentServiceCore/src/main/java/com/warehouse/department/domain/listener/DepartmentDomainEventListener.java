package com.warehouse.department.domain.listener;

import com.warehouse.department.domain.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DepartmentDomainEventListener {

    private final DepartmentService departmentService;

    private final UserClientServicePort userClientServicePort;

    public DepartmentDomainEventListener(final DepartmentService departmentService,
                                         final UserClientServicePort userClientServicePort) {
        this.departmentService = departmentService;
        this.userClientServicePort = userClientServicePort;
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

    @EventListener
    public void handle(final DepartmentDeleted event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
        this.userClientServicePort.notifyUserDepartmentDeleted(snapshot);
    }

    @EventListener
    public void handle(final DepartmentEmailChanged event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
        this.userClientServicePort.notifyUserDepartmentChanged(snapshot);
    }
}
