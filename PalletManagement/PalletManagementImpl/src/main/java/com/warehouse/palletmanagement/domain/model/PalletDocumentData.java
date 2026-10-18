package com.warehouse.palletmanagement.domain.model;

import com.warehouse.palletmanagement.domain.vo.PalletDepartment;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;


public record PalletDocumentData(PalletDetails pallet,
                                 PalletDepartment origin,
                                 PalletDepartment destination,
                                 List<PalletDocumentShipment> shipments,
                                 Instant generatedAt) {

    public PalletDocumentData {
        Objects.requireNonNull(pallet, "pallet");
        shipments = shipments == null ? List.of() : List.copyOf(shipments);
        generatedAt = generatedAt == null ? Instant.now() : generatedAt;
    }

    public BigDecimal cargoWeightInKilograms() {
        return this.shipments.stream()
                .map(PalletDocumentShipment::weightInKilograms)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String documentNumber() {
        return "LP/" + this.pallet.trackingNumber().value();
    }
}
