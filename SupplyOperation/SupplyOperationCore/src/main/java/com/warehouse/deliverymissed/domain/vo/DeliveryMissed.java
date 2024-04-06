package com.warehouse.deliverymissed.domain.vo;


import com.warehouse.deliverymissed.domain.enumeration.DeliveryStatus;
import lombok.Value;

@Value
public class DeliveryMissed {
    String deliveryId;
    Long parcelId;
    String depotCode;
    String supplierCode;
    DeliveryStatus deliveryStatus;
}
