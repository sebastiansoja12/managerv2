package com.warehouse.asyncjob.domain.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.warehouse.asyncjob.domain.model.ReadModelType;

@Component
public class ReadModelRebuilderRegistry {

    private final Map<ReadModelType, ReadModelRebuilder> rebuilders;

    public ReadModelRebuilderRegistry(final List<ReadModelRebuilder> rebuilders) {
        this.rebuilders = new EnumMap<>(ReadModelType.class);
        rebuilders.forEach(rebuilder -> this.rebuilders.put(rebuilder.type(), rebuilder));
    }

    public ReadModelRebuilder rebuilder(final ReadModelType type) {
        final ReadModelRebuilder rebuilder = this.rebuilders.get(type);
        if (rebuilder == null) {
            throw new IllegalArgumentException("Read model rebuilder not found for type: " + type);
        }
        return rebuilder;
    }
}
