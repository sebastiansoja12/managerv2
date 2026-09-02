package com.warehouse.department.domain.listener;

import com.warehouse.department.domain.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.service.DepartmentSyncService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DepartmentDomainEventListener {

    private final DepartmentService departmentService;

    private final DepartmentSyncService departmentSyncService;

    public DepartmentDomainEventListener(final DepartmentService departmentService,
                                         final DepartmentSyncService departmentSyncService) {
        this.departmentService = departmentService;
        this.departmentSyncService = departmentSyncService;
    }

    @EventListener
    public void handle(final DepartmentChanged event) {
        departmentSyncService.syncReadModel(event.getSnapshot());
    }

    @EventListener
    public void handle(final DepartmentAdminChanged event) {
        log.info("Department admin changed event: {}", event.getUserId().getValue());
        this.departmentService.changeAdminUser(event.getDepartmentCode(), event.getUserId());
    }

}
