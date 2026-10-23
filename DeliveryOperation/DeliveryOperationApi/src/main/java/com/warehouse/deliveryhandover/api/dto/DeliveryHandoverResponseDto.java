package com.warehouse.deliveryhandover.api.dto;

import java.util.List;

public record DeliveryHandoverResponseDto(List<DeliveryHandoverResultDto> results, boolean recipientSigned) {
}
