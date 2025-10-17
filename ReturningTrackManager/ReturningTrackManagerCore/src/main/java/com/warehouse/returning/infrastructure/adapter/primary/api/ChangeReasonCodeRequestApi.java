package com.warehouse.returning.infrastructure.adapter.primary.api;

public record ChangeReasonCodeRequestApi(ReturnPackageIdApi returnPackageId, String reasonCode) {
    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
