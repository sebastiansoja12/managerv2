package com.warehouse.auth.infrastructure.adapter.secondary;

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
