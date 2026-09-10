package com.warehouse.deliverycollect.api.dto;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record DeliveryCollectRequestDto(ProcessId processId, List<TrackingNumber> trackingNumbers) {
}
