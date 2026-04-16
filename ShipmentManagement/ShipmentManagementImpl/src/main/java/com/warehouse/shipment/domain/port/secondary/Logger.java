package com.warehouse.shipment.domain.port.secondary;

public interface Logger {
    void info(String format, Object... args);
    void warn(String format, Object... args);
}
