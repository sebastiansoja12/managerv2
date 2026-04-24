package com.warehouse.process.infrastructure.adapter.secondary.mapper;

import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;

public abstract class ProcessLogToModelMapper {

    public static ProcessLog map(final ProcessLogReadEntity readEntity) {
        return ProcessLog.builder().build();
    }
}
