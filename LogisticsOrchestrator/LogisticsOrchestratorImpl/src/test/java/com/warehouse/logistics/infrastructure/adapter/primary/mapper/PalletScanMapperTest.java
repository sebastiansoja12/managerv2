package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanDetail;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.terminal.jaxb.PalletScanRequest;
import com.warehouse.terminal.jaxb.PalletScanResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PalletScanMapperTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("6f1c1d0e-1111-2222-3333-444455556666"));

    private final PalletScanMapper mapper = new PalletScanMapper();

    @Test
    void shouldMapRequestWithTrimmedTrackingNumber() {
        final PalletScanRequest request = new PalletScanRequest();
        request.setPalletTrackingNumber(" PAL000000000001 ");

        final PalletScanCommand command = this.mapper.map(PROCESS_ID, request);

        assertThat(command.processId()).isEqualTo(PROCESS_ID);
        assertThat(command.trackingNumber()).isEqualTo("PAL000000000001");
    }

    @Test
    void shouldMapPartiallyFailedOutcome() {
        final PalletScanOutcome outcome = new PalletScanOutcome("PAL1", "pallet-1", "READY", false, false,
                "SHIPMENT_STATUS_UPDATE_FAILED", "Some shipments could not be updated", List.of(
                new PalletScanDetail(new ShipmentId(1L), true, "SENT", null),
                new PalletScanDetail(new ShipmentId(2L), false, null, "Delivered shipment cannot be changed")));

        final PalletScanResponse response = this.mapper.map(PROCESS_ID, outcome,
                shipmentId -> "TRK-" + shipmentId.getValue());

        assertThat(response.getProcessId()).isEqualTo(PROCESS_ID.value().toString());
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getPalletID()).isEqualTo("pallet-1");
        assertThat(response.getErrorCode()).isEqualTo("SHIPMENT_STATUS_UPDATE_FAILED");
        assertThat(response.getPalletScanResponseDetails().getPalletScanResponseDetail()).hasSize(2);
        assertThat(response.getPalletScanResponseDetails().getPalletScanResponseDetail().get(0).getTrackingNumber())
                .isEqualTo("TRK-1");
        assertThat(response.getPalletScanResponseDetails().getPalletScanResponseDetail().get(1).getErrorMessage())
                .isEqualTo("Delivered shipment cannot be changed");
    }
}
