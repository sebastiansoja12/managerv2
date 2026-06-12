package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import java.util.List;

public record ReturnPageApi(
        List<ReturnPackageApi> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
