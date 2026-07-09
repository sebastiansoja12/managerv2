package com.warehouse.routetracker;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteLogRecordReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void shouldFindRouteLogRecordByProcessId() {
        // given
        final UUID id = UUID.fromString("7ecaa82b-eda9-4b5d-ae9f-933f9adaee27");
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findById(String.valueOf(id));
        // then
        assertTrue(routeLogRecord.isPresent());
    }

    @Test
    void shouldFindRouteLogRecordByParcelId() {
        // given
        final Long id = 123456L;
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findByShipmentId(id);
        // then
        assertTrue(routeLogRecord.isPresent());
    }

    @Test
    void shouldNotFindRouteLogRecordByProcessId() {
        // given
        final UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findById(String.valueOf(id));
        // then
        assertTrue(routeLogRecord.isEmpty());
    }

    @Test
    void shouldNotFindRouteLogRecordByParcelId() {
        // given
        final Long id = 2L;
        // when
        final Optional<RouteLogRecordEntity> routeLogRecord = repository.findByShipmentId(id);
        // then
        assertTrue(routeLogRecord.isEmpty());
    }

    @Test
    void shouldFindAll() {
        // given && when
        final List<RouteLogRecordEntity> routeLogRecord = repository.findAll();
        // then
        assertFalse(routeLogRecord.isEmpty());
    }
}
