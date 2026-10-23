package com.warehouse.deliveryhandover.api.dto;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record DeliveryHandoverRequestDto(ProcessId processId,
                                         List<TrackingNumber> trackingNumbers,
                                         String recipientName,
                                         String signature) {
}
