package com.warehouse.organisationstructure.department.api;

import java.time.LocalDate;

public interface DepartmentReadModelRebuildApiService {

    int rebuildReadModels(Long operatorId, LocalDate dateFrom, LocalDate dateTo);
}
