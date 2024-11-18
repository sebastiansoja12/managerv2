package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.port.secondary.Logger;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class LoggerImpl implements Logger {

    @NonNull
    private final org.slf4j.Logger logger;

    @Override
    public void info(final String format, final Object... args) {
        logger.info(format, args);
    }

    @Override
    public void warn(final String format, final Object... args) {
        logger.warn(format, args);
    }
}
