package com.warehouse.zebra.infrastructure.adapter.primary.strategy;


import java.util.Map;

public class ProcessStrategyFactory {

    private final Map<String, ProcessStrategy> strategies;

    public ProcessStrategyFactory(final Map<String, ProcessStrategy> strategies) {
        this.strategies = strategies;
    }

    public ProcessStrategy getStrategy(final String processType) {
        return strategies.get(processType);
    }
}