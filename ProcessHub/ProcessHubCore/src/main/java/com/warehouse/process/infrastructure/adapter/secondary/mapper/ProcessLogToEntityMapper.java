package com.warehouse.process.infrastructure.adapter.secondary.mapper;

import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;

public abstract class ProcessLogToEntityMapper {
    public static ProcessLogWriteEntity map(final ProcessLog processLog) {
        return ProcessLogWriteEntity.builder().build();
    }
}
