package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Status")
public enum StatusDto {
    CREATED,

    REROUTE,

    SENT,

    DELIVERY,

    RETURN,

    REDIRECT
}
