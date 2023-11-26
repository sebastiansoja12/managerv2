package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReturnStatus")
public enum ReturnStatusDto {
    CREATED, PROCESSING, COMPLETED, CANCELLED
}
