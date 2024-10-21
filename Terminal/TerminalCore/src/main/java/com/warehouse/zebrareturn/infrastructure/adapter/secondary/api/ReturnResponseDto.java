package com.warehouse.zebrareturn.infrastructure.adapter.secondary.api;


import java.util.List;

public record ReturnResponseDto(List<ProcessReturnDto> processReturn) {
}
