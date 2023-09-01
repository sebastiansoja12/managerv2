package com.warehouse.voronoi.domain.service;

public interface LoggerFactory {
    Logger getLogger(Class<?> clazz);

    Logger getLogger(String name);
}
