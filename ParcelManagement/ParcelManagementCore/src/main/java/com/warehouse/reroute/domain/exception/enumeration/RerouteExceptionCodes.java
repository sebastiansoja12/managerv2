package com.warehouse.reroute.domain.exception.enumeration;

import lombok.Getter;

@Getter
public enum RerouteExceptionCodes {

    REROUTE_100(100, "Parcel cannot be rerouted"),

    REROUTE_101(101, "E-mail cannot be null"),

    REROUTE_102(102, "Reroute token expired");


    private final int code;

    private final String message;

    RerouteExceptionCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
