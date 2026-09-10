package com.warehouse.palletmanagement.domain.model;

import com.warehouse.palletmanagement.domain.vo.PalletShipment;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class PalletCargoWeight {

    private static final String KILOGRAMS = "KG";

    private PalletCargoWeight() {
    }

    public static PalletWeight of(final List<PalletShipment> shipments) {
        final BigDecimal total = shipments.stream()
                .map(PalletShipment::weightInKilograms)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PalletWeight(total, KILOGRAMS);
    }
}
