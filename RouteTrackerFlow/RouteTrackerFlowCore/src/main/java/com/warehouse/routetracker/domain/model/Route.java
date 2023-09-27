package com.warehouse.routetracker.domain.model;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Route {

    Long parcelId;

    String username;

    String depotCode;

    String supplierCode;

    public String getUsername() {
        if (StringUtils.isEmpty(username)) {
            return "system";
        }
        return username;
    }
}
