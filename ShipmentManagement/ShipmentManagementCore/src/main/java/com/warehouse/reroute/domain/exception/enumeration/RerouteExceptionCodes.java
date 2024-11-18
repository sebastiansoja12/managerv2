package com.warehouse.reroute.domain.exception.enumeration;

import lombok.Getter;

@Getter
public enum RerouteExceptionCodes {

    REROUTE_403_EMPTY_PARCEL(403, "Parcel cannot be rerouted"),

    REROUTE_500(500, "E-mail cannot be null"),

    REROUTE_403(403, "Reroute token expired");


    private final int code;

    private final String message;

    RerouteExceptionCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
