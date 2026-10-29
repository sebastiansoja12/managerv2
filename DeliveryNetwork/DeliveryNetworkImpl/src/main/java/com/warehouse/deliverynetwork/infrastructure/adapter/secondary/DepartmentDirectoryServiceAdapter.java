package com.warehouse.deliverynetwork.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.domain.vo.DepartmentAddress;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper.DepartmentDirectoryMapper;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<DepartmentAddress> findDepartmentAddress(final DepartmentId departmentId) {
        return Optional.ofNullable(this.departmentApiService.getDepartmentById(departmentId))
                .map(this.departmentDirectoryMapper::toAddress);
    }
}
