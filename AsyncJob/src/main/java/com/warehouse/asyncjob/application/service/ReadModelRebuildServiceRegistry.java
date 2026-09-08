package com.warehouse.asyncjob.application.service;

import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.exception.UnsupportedReadModelTypeException;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReadModelRebuildServiceRegistry {

    private final Map<ReadModelType, ReadModelRebuildServicePort> rebuildServices;

    public ReadModelRebuildServiceRegistry(final List<ReadModelRebuildServicePort> rebuildServices) {
        this.rebuildServices = new EnumMap<>(ReadModelType.class);
        rebuildServices.forEach(rebuildService -> this.rebuildServices.put(rebuildService.type(), rebuildService));
    }

    public ReadModelRebuildServicePort rebuildService(final ReadModelType type) {
        return Optional.ofNullable(this.rebuildServices.get(type))
                .orElseThrow(() -> new UnsupportedReadModelTypeException(type));
    }
}
