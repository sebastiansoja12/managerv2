package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.LoggerFactory;

public class LoggerFactoryImpl implements LoggerFactory {

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new LoggerImpl(org.slf4j.LoggerFactory.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new LoggerImpl(org.slf4j.LoggerFactory.getLogger(name));
    }
}
