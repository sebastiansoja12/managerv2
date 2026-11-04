package com.warehouse.organisationstructure.department.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.List;

public interface DepartmentLifecycleServicePort {

    void notifyCreated(final DepartmentId departmentId);

    List<String> activationBlockers(final DepartmentId departmentId);

    List<String> activeWork(final DepartmentId departmentId);

    void transferWork(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId);
}
