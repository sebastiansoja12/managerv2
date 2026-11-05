package com.warehouse.qrcode.domain.service;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import com.warehouse.qrcode.domain.enumeration.LabelFormat;

public class ShipmentLabelGeneratorFactory {

    private final Map<LabelFormat, ShipmentLabelGenerator> generators = new EnumMap<>(LabelFormat.class);

    public ShipmentLabelGeneratorFactory(final Collection<ShipmentLabelGenerator> generators) {
        generators.forEach(this::register);
    }

    public ShipmentLabelGenerator forFormat(final LabelFormat format) {
        final ShipmentLabelGenerator generator = generators.get(format);
        if (generator == null) {
            throw new IllegalArgumentException("No label generator registered for format " + format);
        }
        return generator;
    }

    private void register(final ShipmentLabelGenerator generator) {
        final ShipmentLabelGenerator previous = generators.putIfAbsent(generator.format(), generator);
        if (previous != null) {
            throw new IllegalStateException("Duplicate label generator for format " + generator.format());
        }
    }
}
