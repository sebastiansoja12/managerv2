package com.warehouse.returning;

import com.warehouse.returning.application.service.ReturnProcessingFixture;
import com.warehouse.returning.application.service.ReturnProcessingService;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.outbox.ReturningOutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ReturnPersistenceTestConfiguration.class, properties = {
        "spring.liquibase.enabled=false", "spring.sql.init.mode=never",
        "manager.kafka.integration-events.routes.return.cancelled=return.cancelled"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ReturnCancellationTransactionTest {
    private final ReturnProcessingService service;
    private final ReturnRepository returns;
    private final ReturningOutboxRepository outbox;
    private final JdbcTemplate jdbc;
    private final TransactionTemplate transactions;

    ReturnCancellationTransactionTest(final ReturnProcessingService service, final ReturnRepository returns,
                                       final ReturningOutboxRepository outbox, final JdbcTemplate jdbc,
                                       final PlatformTransactionManager transactions) {
        this.service = service;
        this.returns = returns;
        this.outbox = outbox;
        this.jdbc = jdbc;
        this.transactions = new TransactionTemplate(transactions);
    }

    @BeforeEach
    void setUp() {
        outbox.deleteAll();
        transactions.executeWithoutResult(status -> returns.createOrUpdate(ReturnProcessingFixture.returning(ReturnStatus.PROCESSING)));
    }

    @Test
    void shouldCommitCancellationAndOneKafkaOutboxMessageTogether() {
        service.cancel(new ReturnPackageId(123L));
        service.cancel(new ReturnPackageId(123L));

        assertEquals(ReturnStatus.CANCELLED, returns.findDetailsById(new ReturnPackageId(123L)).getReturnStatus());
        assertEquals(1L, outbox.count());
        final String payload = jdbc.queryForObject("SELECT payload_json FROM returning_event_outbox", String.class);
        assertTrue(payload.contains("\"eventType\":\"return.cancelled\""));
        assertTrue(payload.contains("\"shipmentId\":{\"value\":456}"));
        assertTrue(payload.contains("\"returnPackageId\":{\"value\":123}"));
        assertTrue(payload.contains("\"departmentId\":{\"value\":3}"));
        assertTrue(payload.contains("\"operatorId\":{\"value\":7}"));
        assertTrue(payload.contains("\"userId\":{\"value\":12}"));
        final String headers = jdbc.queryForObject("SELECT headers_json FROM returning_event_outbox", String.class);
        assertTrue(headers.contains("com.warehouse.returning.api.event.ReturnCancelledIntegrationEvent"));
    }

    @Test
    void shouldRollBackCancellationWhenOutboxCannotBeWritten() {
        jdbc.execute("ALTER TABLE returning_event_outbox ADD CONSTRAINT reject_cancellation CHECK (attempt_count < 0)");
        try {
            assertThrows(RuntimeException.class, () -> service.cancel(new ReturnPackageId(123L)));
            assertEquals(ReturnStatus.PROCESSING, returns.findDetailsById(new ReturnPackageId(123L)).getReturnStatus());
            assertEquals(0L, outbox.count());
        } finally {
            jdbc.execute("ALTER TABLE returning_event_outbox DROP CONSTRAINT reject_cancellation");
        }
    }
}
