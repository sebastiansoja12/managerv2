package com.warehouse.returning.api.dto;

import java.util.List;

public record ReturnPageDto(
        List<ReturnDetailsDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
