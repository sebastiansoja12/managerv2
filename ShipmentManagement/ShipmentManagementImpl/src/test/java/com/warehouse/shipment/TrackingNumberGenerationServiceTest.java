package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.application.service.TrackingNumberGenerationService;
import com.warehouse.shipment.application.service.TrackingNumberSequenceService;
import com.warehouse.shipment.domain.service.TrackingNumberService;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberDateFormat;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackingNumberGenerationServiceTest {

    @Mock
    private TrackingNumberService trackingNumberService;

    @Mock
    private TrackingNumberSequenceService trackingNumberSequenceService;

    @Test
    void shouldObtainSequenceBehindApplicationServiceBoundary() {
        final TrackingNumberRule rule = rule(TrackingNumberSource.SEQUENCE);
        final ShipmentId shipmentId = new ShipmentId(123L);
        final TrackingNumber expected = new TrackingNumber("SHP-0042");
        when(trackingNumberSequenceService.nextValue(rule)).thenReturn(42L);
        when(trackingNumberService.nextTrackingNumber(rule, shipmentId, 42L)).thenReturn(expected);

        final TrackingNumber actual = service().generate(rule, shipmentId);

        assertEquals(expected, actual);
        verify(trackingNumberSequenceService).nextValue(rule);
    }

    @Test
    void shouldNotObtainSequenceForShipmentIdRule() {
        final TrackingNumberRule rule = rule(TrackingNumberSource.SHIPMENT_ID);
        final ShipmentId shipmentId = new ShipmentId(123L);
        final TrackingNumber expected = new TrackingNumber("SHP-123");
        when(trackingNumberService.nextTrackingNumber(rule, shipmentId)).thenReturn(expected);

        final TrackingNumber actual = service().generate(rule, shipmentId);

        assertEquals(expected, actual);
        verify(trackingNumberSequenceService, never()).nextValue(rule);
    }

    private TrackingNumberGenerationService service() {
        return new TrackingNumberGenerationService(trackingNumberService, trackingNumberSequenceService);
    }

    private TrackingNumberRule rule(final TrackingNumberSource source) {
        return new TrackingNumberRule(
                "SHP", "-", source, 4, false, TrackingNumberDateFormat.YYYYMMDD, true);
    }
}
