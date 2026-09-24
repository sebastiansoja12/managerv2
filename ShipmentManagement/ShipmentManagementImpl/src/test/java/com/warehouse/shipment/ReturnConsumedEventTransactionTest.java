package com.warehouse.shipment;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.primary.ReturnProcessingPortImpl;
import com.warehouse.returning.application.port.primary.command.ApplyReturnProcessingStartedCommand;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnConsumedEventServiceAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@EnabledIfEnvironmentVariable(named = "RETURN_TEST_POSTGRES", matches = "true")
class ReturnConsumedEventTransactionTest {
    private final String schema = "return_test_" + UUID.randomUUID().toString().replace("-", "");
    private JdbcTemplate admin;
    private JdbcTemplate jdbc;
    private TransactionTemplate transactions;
    private ReturnConsumedEventServiceAdapter consumed;

    @BeforeEach
    void createIsolatedSchema() {
        final String url = System.getenv("RETURN_TEST_JDBC_URL");
        final String username = System.getenv("RETURN_TEST_JDBC_USER");
        final String password = System.getenv("RETURN_TEST_JDBC_PASSWORD");
        admin = new JdbcTemplate(new DriverManagerDataSource(url, username, password));
        admin.execute("CREATE SCHEMA " + schema);
        final DriverManagerDataSource dataSource = new DriverManagerDataSource(url + "?currentSchema=" + schema, username, password);
        jdbc = new JdbcTemplate(dataSource);
        transactions = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        consumed = new ReturnConsumedEventServiceAdapter(jdbc);
        jdbc.execute("CREATE TABLE return_consumed_event (event_id UUID PRIMARY KEY, consumed_at TIMESTAMP NOT NULL)");
        jdbc.execute("CREATE TABLE return_processing_state (return_package_id BIGINT PRIMARY KEY, cancelled BOOLEAN NOT NULL)");
        jdbc.execute("CREATE TABLE test_shipment (id BIGINT PRIMARY KEY, status VARCHAR(32) NOT NULL)");
        jdbc.update("INSERT INTO test_shipment VALUES (1, 'DELIVERY')");
    }

    @AfterEach
    void removeIsolatedSchema() {
        if (admin != null) {
            admin.execute("DROP SCHEMA IF EXISTS " + schema + " CASCADE");
        }
    }

    @Test
    void shouldRollBackDedupeAndShipmentTogetherAndAllowRetry() {
        final com.warehouse.returning.application.port.secondary.ShipmentServicePort shipments =
                mock(com.warehouse.returning.application.port.secondary.ShipmentServicePort.class);
        final java.util.concurrent.atomic.AtomicBoolean fail = new java.util.concurrent.atomic.AtomicBoolean(true);
        doAnswer(invocation -> {
            jdbc.update("UPDATE test_shipment SET status = 'RETURN' WHERE id = 1");
            if (fail.getAndSet(false)) {
                throw new IllegalStateException("Outbox unavailable");
            }
            return null;
        }).when(shipments).markReturned(new ShipmentId(1L));
        final ReturnProcessingPortImpl port = new ReturnProcessingPortImpl(
                shipments, consumed, new OperatorContext(), transactions);
        final ApplyReturnProcessingStartedCommand command = new ApplyReturnProcessingStartedCommand(
                UUID.randomUUID(), new ShipmentId(1L), new ReturnPackageId(123L), new DepartmentId(3L), new OperatorId(7L), null);

        assertThrows(IllegalStateException.class, () -> port.applyProcessingStarted(command));
        assertEquals(0, jdbc.queryForObject("SELECT count(*) FROM return_consumed_event", Integer.class));
        assertEquals(ShipmentStatus.DELIVERY.name(), jdbc.queryForObject("SELECT status FROM test_shipment", String.class));

        port.applyProcessingStarted(command);
        jdbc.update("UPDATE test_shipment SET status = 'DELIVERY'");
        port.applyProcessingStarted(command);

        assertEquals(1, jdbc.queryForObject("SELECT count(*) FROM return_consumed_event", Integer.class));
        assertEquals("DELIVERY", jdbc.queryForObject("SELECT status FROM test_shipment", String.class));
        verify(shipments, times(2)).markReturned(command.shipmentId());
    }

    @Test
    void shouldConsumeEventOnceWhenConsumersRace() throws Exception {
        final UUID eventId = UUID.randomUUID();
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        final CountDownLatch start = new CountDownLatch(1);
        final Callable<Boolean> consume = () -> {
            start.await();
            return transactions.execute(status -> consumed.tryConsume(eventId));
        };
        try {
            final Future<Boolean> first = executor.submit(consume);
            final Future<Boolean> second = executor.submit(consume);
            start.countDown();
            assertNotEquals(first.get(10, TimeUnit.SECONDS), second.get(10, TimeUnit.SECONDS));
            assertEquals(1, jdbc.queryForObject("SELECT count(*) FROM return_consumed_event", Integer.class));
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldCancelBothShipmentsAndIgnoreLateProcessingAfterRetry() {
        jdbc.update("UPDATE test_shipment SET status = 'RETURN' WHERE id = 1");
        jdbc.update("INSERT INTO test_shipment VALUES (2, 'CREATED')");
        final com.warehouse.returning.application.port.secondary.ShipmentServicePort shipments =
                mock(com.warehouse.returning.application.port.secondary.ShipmentServicePort.class);
        when(shipments.getShipment(new ShipmentId(1L))).thenReturn(
                new com.warehouse.returning.domain.model.ReturnableShipment(
                        new ShipmentId(1L), ShipmentStatus.RETURN, new ShipmentId(2L)));
        doAnswer(invocation -> {
            jdbc.update("UPDATE test_shipment SET status = 'CANCELED' WHERE id = 2");
            return null;
        }).when(shipments).cancelReturnShipment(new ShipmentId(2L));
        final java.util.concurrent.atomic.AtomicBoolean fail = new java.util.concurrent.atomic.AtomicBoolean(true);
        doAnswer(invocation -> {
            jdbc.update("UPDATE test_shipment SET status = 'DELIVERY' WHERE id = 1");
            if (fail.getAndSet(false)) {
                throw new IllegalStateException("Outbox unavailable");
            }
            return null;
        }).when(shipments).restoreAfterReturnCancellation(new ShipmentId(1L));
        final ReturnProcessingPortImpl port = new ReturnProcessingPortImpl(shipments, consumed, new OperatorContext(), transactions);
        final com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand cancelled =
                new com.warehouse.returning.application.port.primary.command.ApplyReturnCancelledCommand(
                        UUID.randomUUID(), new ShipmentId(1L), new ReturnPackageId(123L), new DepartmentId(3L), new OperatorId(7L), null);

        assertThrows(IllegalStateException.class, () -> port.applyCancelled(cancelled));
        assertEquals("RETURN", jdbc.queryForObject("SELECT status FROM test_shipment WHERE id = 1", String.class));
        assertEquals("CREATED", jdbc.queryForObject("SELECT status FROM test_shipment WHERE id = 2", String.class));
        assertEquals(0, jdbc.queryForObject("SELECT count(*) FROM return_consumed_event", Integer.class));

        port.applyCancelled(cancelled);
        port.applyCancelled(cancelled);
        port.applyProcessingStarted(new ApplyReturnProcessingStartedCommand(UUID.randomUUID(), cancelled.shipmentId(),
                cancelled.returnPackageId(), cancelled.departmentId(), cancelled.operatorId(), cancelled.userId()));

        assertEquals("DELIVERY", jdbc.queryForObject("SELECT status FROM test_shipment WHERE id = 1", String.class));
        assertEquals("CANCELED", jdbc.queryForObject("SELECT status FROM test_shipment WHERE id = 2", String.class));
        assertEquals(Boolean.TRUE, jdbc.queryForObject("SELECT cancelled FROM return_processing_state", Boolean.class));
        verify(shipments, never()).markReturned(any());
        verify(shipments, times(2)).cancelReturnShipment(new ShipmentId(2L));
    }
}
