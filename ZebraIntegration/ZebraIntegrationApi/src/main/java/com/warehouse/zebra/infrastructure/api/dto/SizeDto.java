package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Size")
public enum SizeDto {
    TINY,
    SMALL,
    MEDIUM,
    AVERAGE,
    BIG,
    CUSTOM,
    TEST

}
