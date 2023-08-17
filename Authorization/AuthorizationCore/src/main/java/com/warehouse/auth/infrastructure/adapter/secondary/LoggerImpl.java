package com.warehouse.auth.infrastructure.adapter.secondary;

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
}
