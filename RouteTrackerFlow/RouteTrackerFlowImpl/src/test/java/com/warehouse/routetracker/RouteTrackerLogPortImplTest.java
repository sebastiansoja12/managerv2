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

import com.warehouse.routetracker.domain.model.RouteLogRecordDetail;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

@ExtendWith(MockitoExtension.class)
class RouteTrackerLogPortImplTest {

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(100001L);
    private static final LocalDateTime CHANGED_AT = LocalDateTime.of(2026, 8, 9, 12, 0);
    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);
    private static final OperatorId OPERATOR_ID = new OperatorId(7L);
    private static final UserId USER_ID = new UserId(42L);

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

        this.routeTrackerLogPort.createOrChangeShipmentState(command("shipment.changed", ShipmentStatus.CREATED));

        final ArgumentCaptor<RouteLogRecord> recordCaptor = ArgumentCaptor.forClass(RouteLogRecord.class);
        verify(this.repository).save(recordCaptor.capture());
        assertThat(recordCaptor.getValue().getShipmentId()).isEqualTo(SHIPMENT_ID);
        assertThat(recordCaptor.getValue().getRouteLogRecordDetails().getRouteLogRecordDetailSet()).hasSize(1);
        final RouteLogRecordDetail detail = recordCaptor.getValue().getRouteLogRecordDetails()
                .getRouteLogRecordDetailSet().iterator().next();
        assertThat(detail.getEventId()).isNull();
        assertThat(detail.getDescription()).isEqualTo("shipment.changed");
        assertThat(detail.getOperatorId()).isEqualTo(OPERATOR_ID);
        assertThat(detail.getDepartmentId()).isEqualTo(DEPARTMENT_ID);
        assertThat(detail.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void shouldUpdateRouteLogWhenNextShipmentEventIsReceived() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .shipmentId(SHIPMENT_ID)
                .build();
        when(this.repository.findById(SHIPMENT_ID)).thenReturn(Optional.of(routeLogRecord));

        this.routeTrackerLogPort.createOrChangeShipmentState(command("shipment.sent", ShipmentStatus.SENT));

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

    @Test
    void shouldIgnoreRepeatedBusinessEvent() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .shipmentId(SHIPMENT_ID)
                .build();
        when(this.repository.findById(SHIPMENT_ID)).thenReturn(Optional.of(routeLogRecord));

        this.routeTrackerLogPort.createOrChangeShipmentState(command("shipment.changed", ShipmentStatus.CREATED));
        this.routeTrackerLogPort.createOrChangeShipmentState(command("shipment.changed", ShipmentStatus.CREATED));

        assertThat(routeLogRecord.getRouteLogRecordDetails().getRouteLogRecordDetailSet()).hasSize(1);
    }

    private ShipmentStatusStateChangeCommand command(final String eventType, final ShipmentStatus status) {
        return new ShipmentStatusStateChangeCommand(SHIPMENT_ID, eventType, status, CHANGED_AT,
                OPERATOR_ID, DEPARTMENT_ID, USER_ID);
    }
}
