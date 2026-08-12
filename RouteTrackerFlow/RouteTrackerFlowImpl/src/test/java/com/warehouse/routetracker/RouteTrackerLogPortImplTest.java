package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

@ExtendWith(MockitoExtension.class)
class RouteTrackerLogPortImplTest {

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(100001L);
    private static final LocalDateTime OCCURRED_AT = LocalDateTime.of(2026, 8, 9, 12, 0);
    private static final UserId USER_ID = new UserId(42L);
    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);

    @Mock
    private RouteLogRepository repository;

    private RouteTrackerLogPortImpl routeTrackerLogPort;

    @BeforeEach
    void setUp() {
        this.routeTrackerLogPort = new RouteTrackerLogPortImpl(this.repository);
    }

    @Test
    void shouldCreateRouteLogWhenFirstShipmentEventIsReceived() {
        when(this.repository.findById(SHIPMENT_ID)).thenReturn(Optional.empty());

        this.routeTrackerLogPort.createShipmentEvent(
                SHIPMENT_ID, "ShipmentCreatedEvent", ShipmentStatus.CREATED, OCCURRED_AT, "{}",
                USER_ID, DEPARTMENT_ID
        );

        final ArgumentCaptor<RouteLogRecord> recordCaptor = ArgumentCaptor.forClass(RouteLogRecord.class);
        verify(this.repository).save(recordCaptor.capture());
        assertThat(recordCaptor.getValue().getShipmentId()).isEqualTo(SHIPMENT_ID);
        assertThat(recordCaptor.getValue().getRouteLogRecordDetails().getRouteLogRecordDetailSet()).hasSize(1);
        final RouteLogRecordDetail detail = recordCaptor.getValue().getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet().iterator().next();
        assertThat(detail.getDescription()).isEqualTo("ShipmentCreatedEvent");
        assertThat(detail.getUserId()).isEqualTo(USER_ID);
        assertThat(detail.getDepartmentId()).isEqualTo(DEPARTMENT_ID);
    }

    @Test
    void shouldUpdateRouteLogWhenNextShipmentEventIsReceived() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .shipmentId(SHIPMENT_ID)
                .build();
        when(this.repository.findById(SHIPMENT_ID)).thenReturn(Optional.of(routeLogRecord));

        this.routeTrackerLogPort.createShipmentEvent(
                SHIPMENT_ID, "ShipmentSent", ShipmentStatus.SENT, OCCURRED_AT, "{}",
                USER_ID, DEPARTMENT_ID
        );

        verify(this.repository).update(routeLogRecord);
        assertThat(routeLogRecord.getRouteLogRecordDetails().getRouteLogRecordDetailSet()).hasSize(1);
    }

    @Test
    void shouldFindRouteLogByShipmentId() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .shipmentId(SHIPMENT_ID)
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
