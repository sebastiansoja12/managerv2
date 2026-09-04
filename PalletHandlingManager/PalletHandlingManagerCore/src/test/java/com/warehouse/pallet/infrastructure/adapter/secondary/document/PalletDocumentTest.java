package com.warehouse.pallet.infrastructure.adapter.secondary.document;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.enumeration.Unit;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import com.warehouse.pallet.infrastructure.adapter.secondary.enumeration.PalletType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PalletDocumentTest {

    @Test
    void shouldMapPalletToDocumentAndBack() {
        final Pallet pallet = Pallet.withShipments(new PalletId("pallet-1"), new TrackingNumber("PAL1"),
                new OperatorId(7L), List.of(new ShipmentId(1L), new ShipmentId(2L)));
        pallet.changeWeight(new Weight(new BigDecimal("120"), Unit.KG));

        final PalletDocument document = PalletDocument.from(pallet);
        final Pallet mapped = Pallet.from(document);

        assertThat(document.getTrackingNumber()).isEqualTo(new TrackingNumber("PAL1"));
        assertThat(document.getOperatorId()).isEqualTo(new OperatorId(7L));
        assertThat(mapped.getPalletId()).isEqualTo(pallet.getPalletId());
        assertThat(mapped.getTrackingNumber()).isEqualTo(pallet.getTrackingNumber());
        assertThat(mapped.getOperatorId()).isEqualTo(pallet.getOperatorId());
        assertThat(mapped.getShipmentIds()).containsExactlyInAnyOrderElementsOf(pallet.getShipmentIds());
        assertThat(mapped.getPalletStatus()).isEqualTo(PalletStatus.READY);
        assertThat(mapped.getPalletWeight().getValue()).isEqualByComparingTo("120");
        assertThat(mapped.getCreated()).isEqualTo(pallet.getCreated());
    }

    @Test
    void shouldMapEmptyPalletWithoutOptionalFields() {
        final Pallet pallet = Pallet.empty(new PalletId("pallet-2"), new TrackingNumber("PAL2"), new OperatorId(1L));

        final Pallet mapped = Pallet.from(PalletDocument.from(pallet));

        assertThat(mapped.getPalletStatus()).isEqualTo(PalletStatus.CREATED);
        assertThat(mapped.getPalletWeight()).isNull();
        assertThat(mapped.getMaxPalletWeight()).isNull();
    }

    @Test
    void shouldMapLegacyEmptyStatus() {
        final PalletDocument legacyEmpty = legacyDocument(Set.of());
        final PalletDocument legacyWithShipments = legacyDocument(Set.of(new ShipmentId(5L)));

        assertThat(Pallet.from(legacyEmpty).getPalletStatus()).isEqualTo(PalletStatus.CREATED);
        assertThat(Pallet.from(legacyWithShipments).getPalletStatus()).isEqualTo(PalletStatus.READY);
        assertThat(Pallet.from(legacyEmpty).getTrackingNumber()).isNull();
    }

    private PalletDocument legacyDocument(final Set<ShipmentId> shipmentIds) {
        return new PalletDocument(new PalletId("legacy"), null, null, shipmentIds,
                null, null, Instant.now(), Instant.now(),
                PalletStatus.EMPTY, StorageStatus.UNLOCKED, null, null, null, PalletHandlingPriority.LOW,
                PalletType.PARENT, null, false, null);
    }
}
