package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.ShipmentConfigurationServicePort;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.service.TrackingNumberServiceImpl;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberDateFormat;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberSource;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrackingNumberServiceImplTest {

    @Mock
    private ShipmentConfigurationServicePort shipmentConfigurationServicePort;

    private final InMemoryTrackingSequenceRepository sequenceRepository = new InMemoryTrackingSequenceRepository();
    private final TrackingNumberServiceImpl service = new TrackingNumberServiceImpl(shipmentConfigurationServicePort,
            sequenceRepository);

    @Test
    void shouldGenerateTrackingNumberFromSequenceRule() {
        final TrackingNumberRule rule = new TrackingNumberRule(
                "mgr",
                "-",
                TrackingNumberSource.SEQUENCE,
                4,
                true,
                TrackingNumberDateFormat.YYYYMMDD,
                true
        );
        final String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        final TrackingNumber trackingNumber = service.nextTrackingNumber(rule);

        assertEquals("MGR-" + today + "-0001", trackingNumber.value());
    }

    @Test
    void shouldGenerateTrackingNumberFromShipmentIdRule() {
        final TrackingNumberRule rule = new TrackingNumberRule(
                "SHP",
                "/",
                TrackingNumberSource.SHIPMENT_ID,
                8,
                false,
                TrackingNumberDateFormat.YYYYMMDD,
                true
        );

        final TrackingNumber trackingNumber = service.nextTrackingNumber(rule, new ShipmentId(123L));

        assertEquals("SHP/123", trackingNumber.value());
    }

    @Test
    void shouldGenerateTrackingNumberFromRandomRule() {
        final TrackingNumberRule rule = new TrackingNumberRule(
                "rnd",
                "-",
                TrackingNumberSource.RANDOM,
                6,
                false,
                TrackingNumberDateFormat.YYYYMMDD,
                false
        );

        final TrackingNumber trackingNumber = service.nextTrackingNumber(rule);

        assertTrue(trackingNumber.value().matches("rnd-[0-9A-Z]{6}"));
    }

    @Test
    void shouldThrowExceptionWhenShipmentIdRuleHasNoShipmentId() {
        final TrackingNumberRule rule = new TrackingNumberRule(
                "SHP",
                "-",
                TrackingNumberSource.SHIPMENT_ID,
                8,
                false,
                TrackingNumberDateFormat.YYYYMMDD,
                true
        );

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.nextTrackingNumber(rule));

        assertEquals("Shipment id is required for shipment-id based tracking number", exception.getMessage());
    }

    private static class InMemoryTrackingSequenceRepository implements TrackingSequenceRepository {

        private final Map<String, TrackingSequence> sequences = new HashMap<>();

        @Override
        public Optional<TrackingSequence> findById(final String sequenceId) {
            return Optional.ofNullable(sequences.get(sequenceId));
        }

        @Override
        public TrackingSequence save(final TrackingSequence trackingSequence) {
            sequences.put(trackingSequence.getId(), trackingSequence);
            return trackingSequence;
        }
    }
}
