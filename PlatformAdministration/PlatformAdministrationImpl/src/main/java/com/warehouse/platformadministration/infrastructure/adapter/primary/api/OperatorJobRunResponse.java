package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

public record OperatorJobRunResponse(String jobType, boolean asynchronous, int affected, Integer total) {
}
