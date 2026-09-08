package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.terminal.jaxb.CollectDetailType;
import com.warehouse.terminal.jaxb.CollectDetailsType;
import com.warehouse.terminal.jaxb.CollectRequest;
import com.warehouse.terminal.jaxb.CollectResponse;
import com.warehouse.terminal.jaxb.CollectResponseDetailType;

class CollectMapperTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("6f0f3a5e-2d0c-4b8e-9a39-2f7d0d3c1a11"));

    private final CollectMapper collectMapper = new CollectMapper();

    @Test
    void shouldMapTrimmedTrackingNumbersFromCollectRequest() {
        final CollectDetailType collectDetail = new CollectDetailType();
        collectDetail.setTrackingNumber(" TRK-001 ");
        final CollectDetailsType collectDetails = new CollectDetailsType();
        collectDetails.getCollectDetail().add(collectDetail);
        final CollectRequest collectRequest = new CollectRequest();
        collectRequest.setCollectDetails(collectDetails);

        final ShipmentCollectCommand command = collectMapper.map(PROCESS_ID, collectRequest);

        assertAll(
                () -> assertEquals(PROCESS_ID, command.processId()),
                () -> assertEquals(List.of(new TrackingNumber("TRK-001")), command.trackingNumbers())
        );
    }

    @Test
    void shouldReportPartialFailureInCollectResponse() {
        final ShipmentCollectResult result = new ShipmentCollectResult(List.of(
                new ShipmentCollectDetail(new TrackingNumber("TRK-001"), new ShipmentId(100L), true, null),
                new ShipmentCollectDetail(new TrackingNumber("TRK-002"), null, false, "Shipment was not found")));

        final CollectResponse collectResponse = collectMapper.map(PROCESS_ID, result);

        final List<CollectResponseDetailType> details = collectResponse.getCollectResponseDetails().getCollectResponseDetail();
        assertAll(
                () -> assertEquals(PROCESS_ID.value().toString(), collectResponse.getProcessId()),
                () -> assertFalse(collectResponse.isSuccess()),
                () -> assertEquals(2, details.size()),
                () -> assertTrue(details.get(0).isSuccess()),
                () -> assertEquals(100L, details.get(0).getShipmentID()),
                () -> assertFalse(details.get(1).isSuccess()),
                () -> assertNull(details.get(1).getShipmentID()),
                () -> assertEquals("Shipment was not found", details.get(1).getErrorMessage())
        );
    }
}
