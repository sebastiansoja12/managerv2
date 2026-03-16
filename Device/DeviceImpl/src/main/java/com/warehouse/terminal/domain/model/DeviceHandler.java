package com.warehouse.terminal.domain.model;


import com.warehouse.commonassets.enumeration.DeviceType;

public interface DeviceHandler {
    boolean canHandle(final DeviceType deviceType);
}
