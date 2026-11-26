package com.warehouse.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentAssignmentPort;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentOrchestration;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;
import com.warehouse.organisationstructure.department.api.DepartmentReadModelRebuildApiService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourierAssignmentE2ETest extends AbstractE2ETest {

    private static final OperatorId OPERATOR_ID = new OperatorId(1L);
    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(1L);
    private static final long FIRST_COURIER = 920001L;
    private static final long SECOND_COURIER = 920002L;
    private static final long NOT_OPTED_IN_COURIER = 920003L;
    private static final int ELIGIBLE_PARCELS = 27;
    private static final Duration RUN_TIMEOUT = Duration.ofSeconds(30);

    private final DepartmentAssignmentPort departmentAssignmentPort;

    private final OperatorContext operatorContext;

    private final DepartmentReadModelRebuildApiService departmentReadModelRebuildApiService;

    CourierAssignmentE2ETest(@LocalServerPort final int serverPort, final DataSource dataSource,
                             final DepartmentAssignmentPort departmentAssignmentPort,
                             final OperatorContext operatorContext,
                             final DepartmentReadModelRebuildApiService departmentReadModelRebuildApiService) {
        super(serverPort, dataSource);
        this.departmentAssignmentPort = departmentAssignmentPort;
        this.operatorContext = operatorContext;
        this.departmentReadModelRebuildApiService = departmentReadModelRebuildApiService;
    }

    @BeforeAll
    void seedBranchCouriersAndParcels() {
        executeSqlScript(LogisticsE2EFixture.TERMINAL_DEVICE_SEED);
        executeSqlScript("sql/courier-assignment-seed.sql");
        executeSqlScript("sql/courier-assignment-parcels.sql");
        asOperator(() -> departmentReadModelRebuildApiService.rebuildReadModels(OPERATOR_ID.value(),
                LocalDate.now().minusYears(10), LocalDate.now().plusDays(1)));
    }

    @Test
    @Order(1)
    void shouldAllowOnlyOneActiveRunPerDepartment() {
        insertRun(UUID.randomUUID(), 999L, "PENDING", null, null);

        assertThatThrownBy(() -> insertRun(UUID.randomUUID(), 999L, "PROCESSING", null, null))
                .isInstanceOf(DataIntegrityViolationException.class);
        jdbcClient.sql("DELETE FROM department_assignment_run WHERE department_id = 999").update();
    }

    @Test
    @Order(2)
    void shouldAssignEligibleParcelsInKeysetBatches() {
        final AssignmentOrchestration orchestration = asOperator(departmentAssignmentPort::orchestrate);

        final DepartmentAssignmentRunSnapshot run = awaitFinishedRun(1);

        assertThat(orchestration.triggeredDepartments()).isEqualTo(1);
        assertThat(run.status()).isEqualTo(DepartmentAssignmentRunStatus.COMPLETED);
        assertThat(run.summary().assigned()).isEqualTo(ELIGIBLE_PARCELS);
        assertThat(run.summary().noCourier()).isEqualTo(1);
        assertThat(run.summary().skippedNotAtDepartment()).isEqualTo(1);
        assertThat(run.summary().batches()).isGreaterThanOrEqualTo(6);
        assertThat(run.watermark()).isNotNull();
        assertThat(supplierOf(3)).isNull();
        assertThat(supplierOf(4)).isNull();
        assertThat(supplierOf(5)).isEqualTo(NOT_OPTED_IN_COURIER);
        assertThat(assignedParcelsPerCourier().get(FIRST_COURIER) + assignedParcelsPerCourier().get(SECOND_COURIER))
                .isEqualTo(ELIGIBLE_PARCELS);
        assertThat(versionOf(1)).isEqualTo(1L);
    }

    @Test
    @Order(3)
    void shouldPickUpLateArrivingParcelInNextRunThroughBacklogSweep() {
        executeSqlScript("sql/courier-assignment-extra-parcels.sql");
        jdbcClient.sql("UPDATE delivery SET supplier_id = 920003 WHERE id LIKE 'e2e-courier-delivery-%' "
                + "AND CAST(substring(id from 22) AS INT) BETWEEN 32 AND 60").update();

        asOperator(departmentAssignmentPort::orchestrate);
        final DepartmentAssignmentRunSnapshot run = awaitFinishedRun(2);

        assertThat(run.status()).isEqualTo(DepartmentAssignmentRunStatus.COMPLETED);
        assertThat(run.window().backlogFrom()).isBefore(run.window().from());
        assertThat(supplierOf(31)).isIn(FIRST_COURIER, SECOND_COURIER);
    }

    @Test
    @Order(4)
    void shouldResumeFromSavedCursorAfterCrash() {
        jdbcClient.sql("UPDATE delivery SET supplier_id = NULL, version = version + 1 WHERE id IN ("
                + "'e2e-courier-delivery-41', 'e2e-courier-delivery-42', 'e2e-courier-delivery-43', "
                + "'e2e-courier-delivery-44', 'e2e-courier-delivery-45', 'e2e-courier-delivery-46')").update();
        final UUID runId = UUID.randomUUID();
        insertRun(runId, DEPARTMENT_ID.getValue(), "PROCESSING", "e2e-courier-delivery-43", "-1 minute");

        final int redispatched = asOperator(departmentAssignmentPort::recover);
        final DepartmentAssignmentRunSnapshot run = awaitRun(runId);

        assertThat(redispatched).isEqualTo(1);
        assertThat(run.status()).isEqualTo(DepartmentAssignmentRunStatus.COMPLETED);
        assertThat(supplierOf(41)).isNull();
        assertThat(supplierOf(43)).isNull();
        assertThat(supplierOf(44)).isIn(FIRST_COURIER, SECOND_COURIER);
        assertThat(supplierOf(46)).isIn(FIRST_COURIER, SECOND_COURIER);
    }

    @Test
    @Order(5)
    void shouldLetOnlyOneOfConcurrentWorkersProcessRun() {
        jdbcClient.sql("UPDATE delivery SET supplier_id = NULL, version = version + 1 WHERE id IN ("
                + "'e2e-courier-delivery-41', 'e2e-courier-delivery-42', 'e2e-courier-delivery-43')").update();
        final UUID runId = UUID.randomUUID();
        insertRun(runId, DEPARTMENT_ID.getValue(), "PENDING", null, null);
        final ExecutorService workers = Executors.newFixedThreadPool(2);

        try {
            final CompletableFuture<Void> first = CompletableFuture.runAsync(
                    () -> asOperator(() -> process(runId)), workers);
            final CompletableFuture<Void> second = CompletableFuture.runAsync(
                    () -> asOperator(() -> process(runId)), workers);
            CompletableFuture.allOf(first, second).join();
        } finally {
            workers.shutdown();
        }

        final DepartmentAssignmentRunSnapshot run = awaitRun(runId);
        assertThat(run.status()).isEqualTo(DepartmentAssignmentRunStatus.COMPLETED);
        assertThat(run.summary().assigned()).isEqualTo(3);
        assertThat(run.summary().skippedConcurrent()).isZero();
        assertThat(List.of(supplierOf(41), supplierOf(42), supplierOf(43))).doesNotContainNull();
        assertThat(versionOf(41)).isEqualTo(versionOf(42));
    }

    private Void process(final UUID runId) {
        departmentAssignmentPort.process(new DepartmentAssignmentRunId(runId));
        return null;
    }

    private void insertRun(final UUID runId, final Long departmentId, final String status, final String cursorDeliveryId,
                           final String leaseOffset) {
        jdbcClient.sql("""
                        INSERT INTO department_assignment_run (id, version, operator_id, department_id, date_field,
                                                               window_from, window_to, backlog_from, batch_size, status,
                                                               cursor_date, cursor_id, retries, lease_owner, lease_until,
                                                               created_at, updated_at)
                        SELECT CAST(? AS UUID), 0, 1, ?, 'DESTINATION_ARRIVAL', now() - interval '1 hour', now(),
                               now() - interval '1 hour', 2, CAST(? AS VARCHAR),
                               (SELECT current_department_since FROM delivery WHERE id = CAST(? AS VARCHAR)),
                               CAST(? AS VARCHAR), 0,
                               CASE WHEN CAST(? AS VARCHAR) IS NULL THEN NULL ELSE 'crashed-worker' END,
                               CASE WHEN CAST(? AS VARCHAR) IS NULL THEN NULL ELSE now() + CAST(? AS INTERVAL) END,
                               now() - interval '10 minutes', now() - interval '10 minutes'
                        """)
                .param(runId).param(departmentId).param(status).param(cursorDeliveryId).param(cursorDeliveryId)
                .param(leaseOffset).param(leaseOffset).param(leaseOffset)
                .update();
    }

    private DepartmentAssignmentRunSnapshot awaitFinishedRun(final int expectedFinishedRuns) {
        await().atMost(RUN_TIMEOUT).until(() -> departmentRuns().stream()
                .filter(run -> run.status() != DepartmentAssignmentRunStatus.PENDING
                        && run.status() != DepartmentAssignmentRunStatus.PROCESSING)
                .count() >= expectedFinishedRuns);
        return departmentRuns().get(0);
    }

    private DepartmentAssignmentRunSnapshot awaitRun(final UUID runId) {
        await().atMost(RUN_TIMEOUT).until(() -> runById(runId).status() == DepartmentAssignmentRunStatus.COMPLETED);
        return runById(runId);
    }

    private DepartmentAssignmentRunSnapshot runById(final UUID runId) {
        return departmentRuns().stream()
                .filter(run -> run.runId().value().equals(runId))
                .findFirst()
                .orElseThrow();
    }

    private List<DepartmentAssignmentRunSnapshot> departmentRuns() {
        return asOperator(() -> departmentAssignmentPort.findLatestRuns(50)).stream()
                .filter(run -> run.departmentId().equals(DEPARTMENT_ID))
                .toList();
    }

    private <T> T asOperator(final Supplier<T> operation) {
        return operatorContext.runAs(OPERATOR_ID, operation);
    }

    private Long supplierOf(final int parcel) {
        return jdbcClient.sql("SELECT supplier_id FROM delivery WHERE id = ?")
                .param("e2e-courier-delivery-" + parcel)
                .query(Long.class)
                .optional()
                .orElse(null);
    }

    private Long versionOf(final int parcel) {
        return jdbcClient.sql("SELECT version FROM delivery WHERE id = ?")
                .param("e2e-courier-delivery-" + parcel)
                .query(Long.class)
                .single();
    }

    private Map<Long, Long> assignedParcelsPerCourier() {
        return jdbcClient.sql("""
                        SELECT supplier_id, COUNT(*) AS parcels
                        FROM delivery
                        WHERE id LIKE 'e2e-courier-delivery-%' AND supplier_id IS NOT NULL
                        GROUP BY supplier_id""")
                .query((resultSet, rowNumber) -> Map.entry(resultSet.getLong("supplier_id"),
                        resultSet.getLong("parcels")))
                .list()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
