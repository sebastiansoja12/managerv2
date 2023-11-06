package com.warehouse.deliveryreturn.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeliveryStatus")
public enum DeliveryStatusDto {
    DEPOT,
    CLIENT,
    DELIVERY,
    SENDER,
    LOST,
    UNKNOWN,
    DELIVERED,
    REJECTED,
    RETURN
}
