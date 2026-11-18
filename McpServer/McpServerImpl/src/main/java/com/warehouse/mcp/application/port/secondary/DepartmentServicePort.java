package com.warehouse.mcp.application.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.mcp.domain.vo.DepartmentDetails;
import com.warehouse.mcp.domain.vo.DepartmentEntry;
import com.warehouse.mcp.domain.vo.DepartmentAddress;

public interface DepartmentServicePort {
    List<DepartmentEntry> findAllDepartments();
    DepartmentDetails findDepartment(final DepartmentCode departmentCode);
    void changeAddress(final DepartmentCode departmentCode, final DepartmentAddress address);
    void changeType(final DepartmentCode departmentCode, final String type);
    void changeEmail(final DepartmentCode departmentCode, final String email);
    void changeStatus(final DepartmentCode departmentCode, final String status, final String reason);
    void changeActive(final DepartmentCode departmentCode, final boolean active, final String reason);
}
