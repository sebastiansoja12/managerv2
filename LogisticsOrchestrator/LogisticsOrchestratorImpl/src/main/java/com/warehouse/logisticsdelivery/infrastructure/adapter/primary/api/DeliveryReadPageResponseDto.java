package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.util.List;

public record DeliveryReadPageResponseDto(List<DeliveryReadItemResponseDto> items, int page, int size,
                                          boolean hasNext) {
}
