package com.warehouse.auth.infrastructure.adapter.secondary;


public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);

    Logger getLogger(String name);
}