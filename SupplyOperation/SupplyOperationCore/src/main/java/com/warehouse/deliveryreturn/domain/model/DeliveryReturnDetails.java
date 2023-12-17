package com.warehouse.deliveryreturn.domain.model;

import static com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus.RETURN;

import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.exception.WrongDeliveryStatusException;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryReturnDetails {
    private Long parcelId;
    private DeliveryStatus deliveryStatus;
    private String depotCode;
    private String supplierCode;
    private String token;


    public void validateDeliveryStatus() {
        if (!RETURN.equals(deliveryStatus)) {
            throw new WrongDeliveryStatusException(500, "Wrong delivery status");
        }
    }

    public DeliveryReturnDetails updateDeliveryStatus() {
        return DeliveryReturnDetails.builder()
                .deliveryStatus(RETURN)
                .parcelId(parcelId)
                .supplierCode(supplierCode)
                .depotCode(depotCode)
                .build();
    }
}
