package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;

@Service("user.departmentService")
public class DepartmentService {

    private final DepartmentServicePort departmentServicePort;

    public DepartmentService(final DepartmentServicePort departmentServicePort) {
        this.departmentServicePort = departmentServicePort;
    }

    public boolean existsByDepartmentCode(final DepartmentCode departmentCode) {
        return departmentServicePort.departmentExists(departmentCode);
    }

    public boolean existsByDepartmentId(final DepartmentId departmentId, final OperatorId operatorId) {
        return departmentServicePort.departmentExists(departmentId, operatorId);
    }

    public DepartmentId getDepartmentId(final DepartmentCode departmentCode) {
        return departmentServicePort.getDepartmentId(departmentCode);
    }

    public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
        return departmentServicePort.getDepartmentCode(departmentId);
    }
}
