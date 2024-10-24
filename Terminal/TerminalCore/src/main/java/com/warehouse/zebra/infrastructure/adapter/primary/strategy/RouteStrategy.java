package com.warehouse.zebra.infrastructure.adapter.primary.strategy;

import com.warehouse.commonassets.enumeration.ProcessType;

public class RouteStrategy implements ProcessStrategy{
    @Override
    public String process() {
        return ProcessType.ROUTE.name();
    }
}
