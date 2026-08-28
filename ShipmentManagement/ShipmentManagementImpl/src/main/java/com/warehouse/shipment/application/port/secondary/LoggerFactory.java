package com.warehouse.shipment.application.port.secondary;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);

    Logger getLogger(String name);
}
