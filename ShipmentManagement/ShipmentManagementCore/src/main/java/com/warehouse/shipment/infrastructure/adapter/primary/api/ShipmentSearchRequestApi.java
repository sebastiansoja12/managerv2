package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ShipmentSearchRequestApi(
        Long shipmentId,
        String trackingNumber,
        List<ShipmentStatusDto> shipmentStatuses,
        List<ShipmentSizeDto> shipmentSizes,
        List<ShipmentPriorityDto> shipmentPriorities,
        String senderName,
        String recipientName,
        String destination,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String currency,
        Boolean locked,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Integer page,
        Integer size
) {
}
