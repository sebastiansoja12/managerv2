package com.warehouse.department.domain.service;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

public interface DepartmentSyncService {
    void syncReadModel(final DepartmentSnapshot snapshot);

    int syncReadModels();
}
