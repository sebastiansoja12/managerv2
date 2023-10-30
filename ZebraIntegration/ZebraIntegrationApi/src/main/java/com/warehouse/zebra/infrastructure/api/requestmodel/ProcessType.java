package com.warehouse.zebra.infrastructure.api.requestmodel;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProcessType")
public enum ProcessType {
    RETURN, REJECT, ROUTE
}
