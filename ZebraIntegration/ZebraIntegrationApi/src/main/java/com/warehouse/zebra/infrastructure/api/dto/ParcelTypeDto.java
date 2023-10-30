package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ParcelType")
public enum ParcelTypeDto {
    PARENT, CHILD
}
