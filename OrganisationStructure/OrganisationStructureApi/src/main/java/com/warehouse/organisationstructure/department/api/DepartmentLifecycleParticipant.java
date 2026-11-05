package com.warehouse.organisationstructure.department.api;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Optional;

public interface DepartmentLifecycleParticipant {

    default void departmentCreated(final DepartmentId departmentId) {
    }

    default Optional<String> activationBlocker(final DepartmentId departmentId) {
        return Optional.empty();
    }

    default Optional<String> activeWork(final DepartmentId departmentId) {
        return Optional.empty();
    }

    default void transferWork(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
    }
}
