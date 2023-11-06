package com.warehouse.deliveryreturn.domain.model;


import java.util.List;

import com.warehouse.deliveryreturn.domain.enumeration.ProcessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.warehouse.deliveryreturn.domain.enumeration.ProcessType.RETURN;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryReturnRequest {
    private ProcessType processType;
    private DeviceInformation zebraDeviceInformation;
    private List<DeliveryReturnDetails> deliveryReturnDetails;

    public boolean isNotReturn() {
        return deliveryReturnDetails.stream().anyMatch(DeliveryReturnDetails::isNotReturn);
    }

    public void validateStatuses() {
        deliveryReturnDetails.forEach(DeliveryReturnDetails::validateDeliveryStatus);
    }

    public boolean isReturnProcessType() {
        return RETURN.equals(processType);
    }
}
