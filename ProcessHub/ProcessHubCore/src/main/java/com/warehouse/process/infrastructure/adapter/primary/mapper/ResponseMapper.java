package com.warehouse.process.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;

public abstract class ResponseMapper {

    public static ProcessIdDto map(final ProcessId processId) {
        return new ProcessIdDto(processId.value());
    }
}
