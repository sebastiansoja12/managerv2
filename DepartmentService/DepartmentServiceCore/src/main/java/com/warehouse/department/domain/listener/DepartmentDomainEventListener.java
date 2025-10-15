package com.warehouse.department.domain.listener;

import com.warehouse.department.domain.event.DepartmentChanged;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.department.domain.service.DepartmentService;

@Component
public class DepartmentDomainEventListener {

    private final DepartmentService departmentService;

    public DepartmentDomainEventListener(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @EventListener
    public void handle(final DepartmentChanged event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
    }
}
