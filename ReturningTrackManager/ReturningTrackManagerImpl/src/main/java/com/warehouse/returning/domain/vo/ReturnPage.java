package com.warehouse.returning.domain.vo;

import java.util.List;

import com.warehouse.returning.domain.model.ReturnPackage;

public record ReturnPage(
        List<ReturnPackage> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
