package com.warehouse.reroute.domain.port.secondary;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);

    Logger getLogger(String name);
}
