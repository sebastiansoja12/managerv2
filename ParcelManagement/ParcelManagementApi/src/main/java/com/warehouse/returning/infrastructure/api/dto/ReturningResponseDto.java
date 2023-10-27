package com.warehouse.returning.infrastructure.api.dto;

import java.util.List;

public record ReturningResponseDto(List<ProcessReturnDto> processReturn) {
}
