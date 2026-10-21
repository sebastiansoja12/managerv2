package com.warehouse.palletmanagement.infrastructure.adapter.secondary.document;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import com.warehouse.palletmanagement.domain.model.PalletDocumentData;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletDepartment;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PdfPalletDocumentServiceAdapterTest {

    private final PdfPalletDocumentServiceAdapter renderer = new PdfPalletDocumentServiceAdapter(ZoneId.of("Europe/Warsaw"));

    @Test
    void shouldRenderLabelAsPdf() {
        assertThat(header(this.renderer.renderLabel(data()))).isEqualTo("%PDF-");
    }

    @Test
    void shouldRenderTransportDocumentAsPdf() {
        final byte[] document = this.renderer.renderTransportDocument(data());

        assertThat(header(document)).isEqualTo("%PDF-");
        assertThat(document.length).isGreaterThan(1000);
    }

    @Test
    void shouldRenderDocumentsForEmptyPalletWithoutCarrier() {
        final PalletDocumentData data = new PalletDocumentData(PalletFixture.pallet(
                com.warehouse.palletmanagement.domain.enumeration.PalletStatus.CREATED, false, null), null, null,
                List.of(), Instant.now());

        assertThat(header(this.renderer.renderLabel(data))).isEqualTo("%PDF-");
        assertThat(header(this.renderer.renderTransportDocument(data))).isEqualTo("%PDF-");
    }

    private static PalletDocumentData data() {
        final PalletDetails base = PalletFixture.readyPallet(1L);
        final PalletDetails pallet = new PalletDetails(base.palletId(), base.trackingNumber(), base.palletStatus(),
                base.storageStatus(), base.shipmentIds(), false, true, null, new PalletWeight(new BigDecimal("12.5"), "KG"),
                new PalletWeight(new BigDecimal("800"), "KG"), new DepartmentId(10L), new DepartmentId(20L), "S-1",
                base.created(), base.modified(), null, new PalletCarrier(new CarrierId(1L), "Trans-Pol Łódź", TransportType.EXTERNAL_CARRIER,
                null, "Jan Żółkiewski", "500600700", null, "SK 12345", "SK 9876", Instant.now(), "jan"));
        return new PalletDocumentData(pallet,
                new PalletDepartment(new DepartmentId(10L), "KT1", "Katowice", "Mariacka 1", "40-001", "PL"),
                new PalletDepartment(new DepartmentId(20L), "KR1", "Kraków", "Długa 5", "30-001", "PL"),
                List.of(new PalletDocumentShipment(new ShipmentId(1L), "FTM-1", ShipmentStatus.SENT, "Gdańsk",
                        "80-001", new BigDecimal("12.5"))),
                Instant.now());
    }

    private static String header(final byte[] document) {
        return new String(document, 0, 5, StandardCharsets.US_ASCII);
    }
}
