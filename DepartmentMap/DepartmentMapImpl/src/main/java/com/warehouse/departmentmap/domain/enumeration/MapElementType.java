package com.warehouse.departmentmap.domain.enumeration;

public enum MapElementType {
    ROOM,
    WALL,
    AISLE,
    ZONE,
    RACK,
    INBOUND,
    INBOUND_RAMP,
    WORKSTATION,
    SORTER,
    CHUTE,
    RAMP,
    STORAGE_LOCATION;

    public boolean isDirectional() {
        return this == CHUTE || this == RAMP;
    }
}
