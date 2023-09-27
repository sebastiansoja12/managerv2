package com.warehouse.routetracker.domain.model;

import java.util.Map;

import lombok.Value;

@Value
public class ParcelStorage {
    Map<Long, MessageResponse> savedParcelStatusMap;
    String username;
}
