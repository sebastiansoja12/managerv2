package com.warehouse.zebrareturn.infrastructure.adapter.secondary.api;

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
