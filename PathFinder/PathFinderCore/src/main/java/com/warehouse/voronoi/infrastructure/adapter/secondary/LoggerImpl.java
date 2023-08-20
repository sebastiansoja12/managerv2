package com.warehouse.voronoi.infrastructure.adapter.secondary;


import com.warehouse.voronoi.domain.service.Logger;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class LoggerImpl implements Logger {

    @NonNull
    private final org.slf4j.Logger logger;

    @Override
    public void info(String format, Object... args) {
        logger.info(format, args);
    }

    @Override
    public void warn(String format, Object... args) {
        logger.warn(format, args);
    }
}
