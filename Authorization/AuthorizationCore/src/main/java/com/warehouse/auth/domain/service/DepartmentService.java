package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.auth.infrastructure.adapter.secondary.DepartmentUserReadRepository;
import com.warehouse.commonassets.identificator.DepartmentCode;

@Service("user.departmentService")
public class DepartmentService {
    private final DepartmentUserReadRepository departmentUserReadRepository;

    public DepartmentService(final DepartmentUserReadRepository departmentUserReadRepository) {
        this.departmentUserReadRepository = departmentUserReadRepository;
    }

    public boolean existsByDepartmentCode(final DepartmentCode departmentCode) {
        return departmentUserReadRepository.existsByDepartmentCode(departmentCode.value());
    }
}
