package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReturnStatus")
public enum ReturnStatus {
    CREATED, PROCESSING, COMPLETED, CANCELLED
}
