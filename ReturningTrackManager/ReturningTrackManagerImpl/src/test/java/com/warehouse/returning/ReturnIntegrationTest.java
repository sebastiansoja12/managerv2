package com.warehouse.returning;

import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.exception.StatusChangeException;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.*;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnToken;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.enumeration.Status;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.ReturnPackageNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = ReturnPersistenceTestConfiguration.class, properties = {
        "spring.liquibase.enabled=false", "spring.sql.init.mode=never",
        "manager.kafka.integration-events.routes.return.processing.started=return.processing.started",
        "manager.kafka.integration-events.routes.return.cancelled=return.cancelled"
})
@Transactional
class ReturnIntegrationTest {

    @Autowired
    private ReturnPort returnPort;

    @Autowired
    private ReturnReadRepository repository;

    @Autowired
    private ReturnRepository returnRepository;

    @BeforeEach
    void setupTestData() {
        createReturnPackageEntity(
                1001L,
                5001L,
                "Zwrot — niepotrzebny",
                Status.COMPLETED,
                "ABC123TOKEN",
                new DepartmentId(1L),
                new DepartmentId(1L),
                1L,
                2L,
                ReasonCode.NO_LONGER_NEEDED,
                Instant.parse("2025-10-12T12:00:00Z"),
                Instant.parse("2025-10-12T12:00:00Z")
        );

        createReturnPackageEntity(
                1002L,
                5002L,
                "Uszkodzony produkt",
                Status.CANCELLED,
                "XYZ789TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );
    }

    @AfterEach
    void cleanupTestData() {
        repository.deleteAll();
    }

    @Test
    void shouldProcessRequest() {
        final DepartmentId issuerDepartmentId = new DepartmentId(5L);
        final UserId issuerUserId = new UserId(1L);
        final List<ReturnPackageRequest> requests = buildReturnPackageRequest(issuerDepartmentId, issuerUserId,
                new ShipmentId(1L), "Zwrot", ReasonCode.NO_LONGER_NEEDED);
        final ReturnRequest request = new ReturnRequest(issuerDepartmentId, issuerUserId, requests);

        final ReturnResponse response = this.returnPort.process(request);

        assertNotNull(response.processReturn());
    }

    @Test
    void shouldPersistOperatorIdWhenProcessingRequest() {
        final DepartmentId issuerDepartmentId = new DepartmentId(5L);
        final UserId issuerUserId = new UserId(1L);
        final OperatorId operatorId = new OperatorId(77L);
        final List<ReturnPackageRequest> requests = buildReturnPackageRequest(
                issuerDepartmentId, issuerUserId, new ShipmentId(9001L), "Zwrot", ReasonCode.DAMAGED);

        final ReturnResponse response = this.returnPort.process(
                new ReturnRequest(issuerDepartmentId, issuerUserId, operatorId, requests));

        final ReturnPackage persisted = returnRepository.findById(
                new ReturnPackageId(response.processReturn().get(0).returnId().getValue()));
        assertEquals(operatorId, persisted.getOperatorId());
    }

    @Test
    void shouldListOnlyReturnsForDepartmentAndOperator() {
        createReturnPackageEntity(
                2001L, 6001L, "Zwrot operatora 77", Status.CREATED, "TOKEN77", new DepartmentId(4L), new DepartmentId(4L),
                11L, 12L, ReasonCode.DAMAGED, 77L,
                Instant.parse("2026-08-14T08:00:00Z"), Instant.parse("2026-08-14T09:00:00Z"));
        createReturnPackageEntity(
                2002L, 6002L, "Zwrot operatora 88", Status.CREATED, "TOKEN88", new DepartmentId(4L), new DepartmentId(4L),
                21L, 22L, ReasonCode.DAMAGED, 88L,
                Instant.parse("2026-08-14T10:00:00Z"), Instant.parse("2026-08-14T11:00:00Z"));

        final ReturnPage result = this.returnPort.getReturns(new DepartmentId(4L), new OperatorId(77L), 0, 50);

        assertEquals(1, result.totalElements());
        assertEquals(2001L, result.content().get(0).getReturnPackageId().value());
        assertEquals(new OperatorId(77L), result.content().get(0).getOperatorId());
    }

    @Test
    void shouldSkipProcessingRequestWhenShipmentAlreadyIsRegistered() {
        final DepartmentId issuerDepartmentId = new DepartmentId(5L);
        final UserId issuerUserId = new UserId(1L);
        final List<ReturnPackageRequest> requests = buildReturnPackageRequest(issuerDepartmentId, issuerUserId,
                new ShipmentId(5001L), "Zwrot", ReasonCode.NO_LONGER_NEEDED);
        final ReturnRequest request = new ReturnRequest(issuerDepartmentId, issuerUserId, requests);

        final ReturnResponse response = this.returnPort.process(request);

        assertEquals(1, response.processReturn().size());
    }

    @Test
    void shouldChangeReasonCode() {
        final ReturnPackageId returnPackageId = new ReturnPackageId(1001L);
        final ChangeReasonCodeRequest request = new ChangeReasonCodeRequest(returnPackageId, ReasonCode.NO_LONGER_NEEDED);

        this.returnPort.changeReasonCode(request);

        final ReturnPackage returnPackage = returnRepository.findById(returnPackageId);
        assertEquals(ReasonCode.NO_LONGER_NEEDED, returnPackage.getReasonCode());
    }

    @Test
    void shouldCompleteReturn() {
        final ReturnPackageEntity entity = createReturnPackageEntity(
                123L,
                15L,
                "Uszkodzony produkt",
                Status.PROCESSING,
                "XYZ789TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );


        this.returnPort.complete(new ReturnPackageId(123L));

        assertEquals(Status.COMPLETED, entity.getReturnStatus());
    }

    @Test
    void shouldStartProcessingReturn() {
        final ReturnPackageEntity entity = createReturnPackageEntity(
                125L,
                17L,
                "Uszkodzony produkt",
                Status.CREATED,
                "PROCESS123TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );


        this.returnPort.startProcessing(new ReturnPackageId(125L));

        assertEquals(Status.PROCESSING, entity.getReturnStatus());
    }

    @Test
    void shouldCancelReturnByReturnPackageId() {
        final ReturnPackageEntity entity = createReturnPackageEntity(
                124L,
                16L,
                "Uszkodzony produkt",
                Status.PROCESSING,
                "XYZ789TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );


        this.returnPort.delete(new ReturnPackageId(124L));

        assertEquals(Status.CANCELLED, entity.getReturnStatus());
    }

    @ParameterizedTest
    @CsvSource({"1001"})
    void shouldGetReturn(final String returnId) {
        final ReturnPackage returnPackage = this.returnPort.getReturn(new ReturnPackageId(Long.parseLong(returnId)));
        assertNotNull(returnPackage);
    }

    @Test
    void shouldNotGetReturn() {
        final Executable executable = () -> this.returnPort.getReturn(new ReturnPackageId(1L));
        final ReturnPackageNotFoundException exception = assertThrows(ReturnPackageNotFoundException.class, executable);
        assertEquals("Return package not found", exception.getMessage());
    }

    @Test
    void shouldDeleteReturn() {
        final ReturnPackageId returnPackageId = new ReturnPackageId(1001L);
        final ReturnPackageEntity entity = createReturnPackageEntity(
                1001L,
                15L,
                "Uszkodzony produkt",
                Status.PROCESSING,
                "XYZ789TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );

        this.returnPort.delete(returnPackageId);

        assertEquals(Status.CANCELLED, entity.getReturnStatus());
    }

    @Test
    void shouldNotDeleteReturnWhenStatusIsAlreadyCompleted() {
        final ReturnPackageId returnPackageId = new ReturnPackageId(1001L);
        createReturnPackageEntity(
                1001L,
                15L,
                "Uszkodzony produkt",
                Status.COMPLETED,
                "XYZ789TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );

        final Executable executable = () -> this.returnPort.delete(returnPackageId);
        final StatusChangeException exception = assertThrows(StatusChangeException.class, executable);

        assertEquals("Return package is already completed, cannot override status", exception.getMessage());
    }

    @Test
    void shouldReadCancelledReturnDetailsWithoutMakingItAvailableForOperations() {
        final ReturnPackageId returnPackageId = new ReturnPackageId(1001L);
        createReturnPackageEntity(
                1001L,
                15L,
                "Uszkodzony produkt",
                Status.CANCELLED,
                "XYZ789TOKEN",
                new DepartmentId(2L),
                new DepartmentId(3L),
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );

        assertEquals(ReturnStatus.CANCELLED, this.returnPort.getReturn(returnPackageId).getReturnStatus());

        final Executable executable = () -> this.returnRepository.findById(returnPackageId);
        final ReturnPackageNotFoundException exception = assertThrows(ReturnPackageNotFoundException.class, executable);

        assertEquals("Return package not found", exception.getMessage());
    }

	private List<ReturnPackageRequest> buildReturnPackageRequest(final DepartmentId departmentId,
			final UserId userId, final ShipmentId shipmentId, final String reason, final ReasonCode reasonCode) {
		final ReturnPackageRequest request = new ReturnPackageRequest(departmentId, reason, shipmentId, userId,
				reasonCode);
		return List.of(request);
	}

    private ReturnPackageEntity createReturnPackageEntity(
            final Long returnId, final Long shipmentId, final String reason, final Status status,
            final String returnToken, final DepartmentId assignedDepartment, final DepartmentId returnedDepartment,
            final Long assignedTo, final Long processedBy, final ReasonCode reasonCode,
            final Instant createdAt, final Instant updatedAt) {
        return createReturnPackageEntity(returnId, shipmentId, reason, status, returnToken,
                assignedDepartment, returnedDepartment, assignedTo, processedBy, reasonCode, 7L, createdAt, updatedAt);
    }

    private ReturnPackageEntity createReturnPackageEntity(
            final Long returnId,
            final Long shipmentId,
            final String reason,
            final Status status,
            final String returnToken,
            final DepartmentId assignedDepartment,
            final DepartmentId returnedDepartment,
            final Long assignedTo,
            final Long processedBy,
            final ReasonCode reasonCode,
            final Long operatorId,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        final ReturnPackageEntity entity = new ReturnPackageEntity(
                new ReturnId(returnId),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(shipmentId),
                reason,
                status,
                new ReturnToken(returnToken),
                assignedDepartment,
                returnedDepartment,
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId(assignedTo),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId(processedBy),
                reasonCode,
                new OperatorId(operatorId),
                createdAt,
                updatedAt
        );

        return repository.save(entity);
    }

}
