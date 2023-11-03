package com.warehouse.deliveryreturn.domain.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryReturnRequest {
    private DeviceInformation deviceInformation;
    private List<DeliveryReturnDetails> deliveries;

    public boolean isNotReturn() {
        return deliveries.stream().anyMatch(DeliveryReturnDetails::isNotReturn);
    }

    public void validateStatuses() {
        deliveries.forEach(DeliveryReturnDetails::validateDeliveryStatus);
    }
}
