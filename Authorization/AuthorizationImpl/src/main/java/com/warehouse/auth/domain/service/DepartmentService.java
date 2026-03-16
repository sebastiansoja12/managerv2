package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.commonassets.identificator.DepartmentCode;

@Service("user.departmentService")
public class DepartmentService {

    private final DepartmentServicePort departmentServicePort;

    public DepartmentService(final DepartmentServicePort departmentServicePort) {
        this.departmentServicePort = departmentServicePort;
    }

    public boolean existsByDepartmentCode(final DepartmentCode departmentCode) {
        return departmentServicePort.departmentExists(departmentCode);
    }
}
