package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

@ExtendWith(MockitoExtension.class)
class RouteTrackerLogPortImplTest {

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(100001L);
    private static final LocalDateTime OCCURRED_AT = LocalDateTime.of(2026, 8, 9, 12, 0);

    @Mock
    private RouteLogRepository repository;

    private RouteTrackerLogPortImpl routeTrackerLogPort;

    @BeforeEach
    void setUp() {
        this.routeTrackerLogPort = new RouteTrackerLogPortImpl(this.repository);
    }

    @Test
    void shouldCreateRouteLogWhenFirstShipmentEventIsReceived() {
        when(this.repository.findOptional(SHIPMENT_ID)).thenReturn(Optional.empty());

        this.routeTrackerLogPort.saveShipmentEvent(
                SHIPMENT_ID, "ShipmentCreatedEvent", ParcelStatus.CREATED, OCCURRED_AT, "{}"
        );

        final ArgumentCaptor<RouteLogRecord> recordCaptor = ArgumentCaptor.forClass(RouteLogRecord.class);
        verify(this.repository).save(recordCaptor.capture());
        assertThat(recordCaptor.getValue().getParcelId()).isEqualTo(SHIPMENT_ID.value());
        assertThat(recordCaptor.getValue().getRouteLogRecordDetails().getRouteLogRecordDetailSet()).hasSize(1);
    }

    @Test
    void shouldUpdateRouteLogWhenNextShipmentEventIsReceived() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .parcelId(SHIPMENT_ID.value())
                .build();
        when(this.repository.findOptional(SHIPMENT_ID)).thenReturn(Optional.of(routeLogRecord));

        this.routeTrackerLogPort.saveShipmentEvent(
                SHIPMENT_ID, "ShipmentSent", ParcelStatus.SENT, OCCURRED_AT, "{}"
        );

        verify(this.repository).update(routeLogRecord);
        assertThat(routeLogRecord.getRouteLogRecordDetails().getRouteLogRecordDetailSet()).hasSize(1);
    }

    @Test
    void shouldFindRouteLogByShipmentId() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .parcelId(SHIPMENT_ID.value())
                .build();
        when(this.repository.find(SHIPMENT_ID)).thenReturn(routeLogRecord);

        final RouteLogRecord result = this.routeTrackerLogPort.find(SHIPMENT_ID);

        assertThat(result).isSameAs(routeLogRecord);
    }

    @Test
    void shouldFindAllRouteLogs() {
        final List<RouteLogRecord> routeLogRecords = List.of(RouteLogRecord.builder().build());
        when(this.repository.findAll()).thenReturn(routeLogRecords);

        final List<RouteLogRecord> result = this.routeTrackerLogPort.findAll();

        assertThat(result).isSameAs(routeLogRecords);
    }
}
