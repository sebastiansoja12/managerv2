package com.warehouse.returning.infrastructure.adapter.primary.api;

import com.warehouse.returning.domain.vo.ReturnPackageId;

public record ChangeReturnStatusApiRequest(ReturnPackageId returnPackageId, String returnStatus) {

    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
