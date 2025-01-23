package com.warehouse.deliveryreturn.infrastructure.api.request;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProcessType")
public enum ProcessType {
    RETURN, REJECT, ROUTE
}