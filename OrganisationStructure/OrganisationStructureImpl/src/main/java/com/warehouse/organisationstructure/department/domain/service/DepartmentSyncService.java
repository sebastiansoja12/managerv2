package com.warehouse.organisationstructure.department.domain.service;

import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

public interface DepartmentSyncService {
    void syncReadModel(final DepartmentSnapshot snapshot);

    int syncReadModels();
}
