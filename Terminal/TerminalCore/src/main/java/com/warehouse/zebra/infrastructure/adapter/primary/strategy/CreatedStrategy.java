package com.warehouse.zebra.infrastructure.adapter.primary.strategy;

import com.warehouse.commonassets.enumeration.ProcessType;

public class CreatedStrategy implements ProcessStrategy {

    @Override
    public String process() {
        return ProcessType.CREATED.name();
    }
}
