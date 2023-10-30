package com.warehouse.zebra.infrastructure.adapter.secondary.api;

import com.warehouse.zebra.infrastructure.api.dto.ProcessReturnDto;

import java.util.List;

public record ReturnResponseDto(List<ProcessReturnDto> processReturn) {
}
