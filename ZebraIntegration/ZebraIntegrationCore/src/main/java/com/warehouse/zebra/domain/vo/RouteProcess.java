package com.warehouse.zebra.domain.vo;

import lombok.Value;

import java.util.UUID;

@Value
public class RouteProcess {
    Long parcelId;
    UUID processId;
}
