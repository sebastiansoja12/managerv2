package com.warehouse.terminal.enumeration;


import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeliveryStatus")
public enum DeliveryStatus {
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
