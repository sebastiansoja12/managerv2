package com.warehouse.routetracker;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteLogRecordReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordDetailId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteLogToEntityMapper;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import static org.mapstruct.factory.Mappers.getMapper;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/routelogrecord_db.xml")
public class RouteLogRecordReadRepositoryTest {

    @Autowired
    private RouteLogRecordReadRepository repository;

    private final RouteLogToEntityMapper entityMapper = getMapper(RouteLogToEntityMapper.class);

    @Test
    void shouldPersistDetailWithRouteLogRecordIdInInsert() {
        final RouteLogRecord routeLogRecord = RouteLogRecord.builder()
                .shipmentId(new ShipmentId(345678L))
                .build();
        routeLogRecord.createShipmentEvent(new ShipmentStatusStateChangeCommand(
                routeLogRecord.getShipmentId(),
                "shipment.changed",
                ShipmentStatus.CREATED,
                LocalDateTime.now(),
                new OperatorId(3L),
                new DepartmentId(30L),
                new UserId(4L)));

        final RouteLogRecordEntity saved = repository.saveAndFlush(entityMapper.map(routeLogRecord));
        final RouteLogRecordDetailEntity detail = saved.getRouteLogRecordDetails().getFirst();

        assertNotNull(saved.getId());
        assertEquals(saved.getId(), detail.getRouteLogRecord().getId());
        assertEquals(new com.warehouse.routetracker.infrastructure.adapter.secondary.entity.OperatorId(3L),
                detail.getOperatorId());
    }

    @Test
    void shouldFindRouteLogRecordByProcessId() {
        // given
        final UUID id = UUID.fromString("7ecaa82b-eda9-4b5d-ae9f-933f9adaee27");
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findById(
                new RouteLogRecordId(String.valueOf(id)));
        // then
        assertTrue(routeLogRecord.isPresent());
    }

    @Test
    void shouldFindRouteLogRecordByShipmentId() {
        // given
        final Long id = 123456L;
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findByShipmentId(new ShipmentId(id));
        // then
        assertTrue(routeLogRecord.isPresent());
    }

    @Test
    void shouldLoadDetailIdentifiersWithoutEntityRelations() {
        final RouteLogRecordEntity routeLogRecord = repository
                .findByShipmentId(new ShipmentId(123456L))
                .orElseThrow();

        assertTrue(Hibernate.isInitialized(routeLogRecord.getRouteLogRecordDetails()));
        assertEquals(new ShipmentId(123456L), routeLogRecord.getShipmentId());
        final RouteLogRecordDetailEntity detail = routeLogRecord.getRouteLogRecordDetails().getFirst();

        assertEquals(new RouteLogRecordDetailId(1L), detail.getId());
        assertEquals(routeLogRecord.getId(), detail.getRouteLogRecord().getId());
        assertEquals(new com.warehouse.routetracker.infrastructure.adapter.secondary.entity.UserId(1L),
                detail.getUserId());
        assertEquals(new com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepartmentId(10L),
                detail.getDepartmentId());
        assertEquals(new com.warehouse.routetracker.infrastructure.adapter.secondary.entity.SupplierId(100L),
                detail.getSupplierId());
    }

    @Test
    void shouldNotFindRouteLogRecordByProcessId() {
        // given
        final UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findById(
                new RouteLogRecordId(String.valueOf(id)));
        // then
        assertTrue(routeLogRecord.isEmpty());
    }

    @Test
    void shouldNotFindRouteLogRecordByShipmentId() {
        // given
        final Long id = 2L;
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findByShipmentId(new ShipmentId(id));
        // then
        assertTrue(routeLogRecord.isEmpty());
    }

    @Test
    void shouldFindAll() {
        // given && when
        final List<RouteLogRecordEntity> routeLogRecord = repository.findAll();
        // then
        assertFalse(routeLogRecord.isEmpty());
        assertTrue(routeLogRecord.stream()
                .allMatch(record -> Hibernate.isInitialized(record.getRouteLogRecordDetails())));
    }
}
