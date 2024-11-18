package com.warehouse.shipment.domain.port.secondary;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);

    Logger getLogger(String name);
}
