package com.warehouse.returning.infrastructure.adapter.primary.api.dto;


import java.util.List;

import lombok.Builder;

@Builder
public record ReturnRequestApi(
        List<ReturnPackageRequestApi> requests,
        DepartmentCodeApi departmentCode,
        UserIdApi userId
) {}
