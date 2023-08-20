package com.warehouse.voronoi.domain.service;

public interface Logger {
    void info(String format, Object... args);

    void warn(String format, Object... args);
}
