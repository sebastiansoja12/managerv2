package com.warehouse.deliveryreturn.domain.model;

import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.exception.WrongDeliveryStatusException;
import lombok.Builder;
import lombok.Data;

import static com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus.DELIVERY;
import static com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus.RETURN;

@Data
@Builder
public class DeliveryReturnDetails {
    private Long parcelId;
    private String depotCode;
    private String supplierCode;
    private DeliveryStatus deliveryStatus;


    public void validateDeliveryStatus() {
        if (!DELIVERY.equals(deliveryStatus)) {
            throw new WrongDeliveryStatusException(7000, "Wrong delivery status");
        }
    }

    public boolean isNotReturn() {
        return !RETURN.equals(deliveryStatus);
    }

    public DeliveryReturnDetails updateDeliveryStatus() {
        return DeliveryReturnDetails.builder()
                .deliveryStatus(RETURN)
                .depotCode(depotCode)
                .supplierCode(supplierCode)
                .parcelId(parcelId)
                .build();
    }
}
