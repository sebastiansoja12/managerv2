package com.warehouse.department.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.department.domain.event.DepartmentCreated;
import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

@Component
public class DepartmentDomainEventListener {

    private final DepartmentService departmentService;

    private final TenantAdminProvisioningPort tenantAdminProvisioningPort;

    public DepartmentDomainEventListener(final DepartmentService departmentService,
                                         final TenantAdminProvisioningPort tenantAdminProvisioningPort) {
        this.departmentService = departmentService;
        this.tenantAdminProvisioningPort = tenantAdminProvisioningPort;
    }

    @EventListener
    public void handle(final DepartmentCreated event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
        this.tenantAdminProvisioningPort.createInitialAdminUser(snapshot);
    }
}
