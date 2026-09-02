package com.warehouse.deliverynetwork.infrastructure.adapter.secondary;

import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper.DepartmentDirectoryMapper;
import com.warehouse.department.api.DepartmentApiService;

import java.util.List;

public class DepartmentDirectoryServiceAdapter implements DepartmentDirectoryServicePort {

    private final DepartmentApiService departmentApiService;

    private final DepartmentDirectoryMapper departmentDirectoryMapper;

    public DepartmentDirectoryServiceAdapter(
            final DepartmentApiService departmentApiService,
            final DepartmentDirectoryMapper departmentDirectoryMapper) {
        this.departmentApiService = departmentApiService;
        this.departmentDirectoryMapper = departmentDirectoryMapper;
    }

    @Override
    public List<DepartmentNode> getCurrentOperatorDepartments() {
        return this.departmentApiService.getDepartmentDirectory()
                .stream()
                .map(this.departmentDirectoryMapper::toModel)
                .toList();
    }
}
