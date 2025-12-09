package com.warehouse.returning;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.exception.StatusChangeException;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
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


@SpringBootTest
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
                "KT1",
                "KT1",
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
                "KT2",
                "KT3",
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
        final DepartmentCode issuerDepartmentCode = new DepartmentCode("TST");
        final UserId issuerUserId = new UserId(1L);
        final List<ReturnPackageRequest> requests = buildReturnPackageRequest(issuerDepartmentCode, issuerUserId, 
                new ShipmentId(1L), "Zwrot", ReasonCode.NO_LONGER_NEEDED);
        final ReturnRequest request = new ReturnRequest(issuerDepartmentCode, issuerUserId, requests);

        final ReturnResponse response = this.returnPort.process(request);

        assertNotNull(response.processReturn());
    }

    @Test
    void shouldSkipProcessingRequestWhenShipmentAlreadyIsRegistered() {
        final DepartmentCode issuerDepartmentCode = new DepartmentCode("TST");
        final UserId issuerUserId = new UserId(1L);
        final List<ReturnPackageRequest> requests = buildReturnPackageRequest(issuerDepartmentCode, issuerUserId,
                new ShipmentId(5001L), "Zwrot", ReasonCode.NO_LONGER_NEEDED);
        final ReturnRequest request = new ReturnRequest(issuerDepartmentCode, issuerUserId, requests);

        final ReturnResponse response = this.returnPort.process(request);

        assertEquals(0, response.processReturn().size());
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
                "KT2",
                "KT3",
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );

        this.returnPort.complete(new ReturnPackageId(123L));

        assertEquals(Status.COMPLETED, entity.getReturnStatus());
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
                "KT2",
                "KT3",
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
                "KT2",
                "KT3",
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
    void shouldNotFindAnyReturnPackageWhenIsCancelled() {
        final ReturnPackageId returnPackageId = new ReturnPackageId(1001L);
        createReturnPackageEntity(
                1001L,
                15L,
                "Uszkodzony produkt",
                Status.CANCELLED,
                "XYZ789TOKEN",
                "KT2",
                "KT3",
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );

        final Executable executable = () -> this.returnPort.getReturn(returnPackageId);
        final ReturnPackageNotFoundException exception = assertThrows(ReturnPackageNotFoundException.class, executable);

        assertEquals("Return package not found", exception.getMessage());
    }

    @Test
    void shouldNotCompleteWhenIsCancelled() {
        final ReturnPackageId returnPackageId = new ReturnPackageId(1001L);
        final ReturnPackageEntity entity = createReturnPackageEntity(
                1001L,
                15L,
                "Uszkodzony produkt",
                Status.CANCELLED,
                "XYZ789TOKEN",
                "KT2",
                "KT3",
                3L,
                4L,
                ReasonCode.DAMAGED,
                Instant.parse("2025-10-12T12:10:00Z"),
                Instant.parse("2025-10-12T12:30:00Z")
        );

        final Executable executable = () -> this.returnPort.complete(returnPackageId);
        final ReturnPackageNotFoundException exception = assertThrows(ReturnPackageNotFoundException.class, executable);

        assertEquals("Return package not found", exception.getMessage());
        assertEquals(Status.CANCELLED, entity.getReturnStatus());
    }

	private List<ReturnPackageRequest> buildReturnPackageRequest(final DepartmentCode departmentCode,
			final UserId userId, final ShipmentId shipmentId, final String reason, final ReasonCode reasonCode) {
		final ReturnPackageRequest request = new ReturnPackageRequest(departmentCode, reason, shipmentId, userId,
				reasonCode);
		return List.of(request);
	}

    private ReturnPackageEntity createReturnPackageEntity(
            final Long returnId,
            final Long shipmentId,
            final String reason,
            final Status status,
            final String returnToken,
            final String assignedDepartment,
            final String returnedDepartment,
            final Long assignedTo,
            final Long processedBy,
            final ReasonCode reasonCode,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        final ReturnPackageEntity entity = new ReturnPackageEntity(
                new ReturnId(returnId),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(shipmentId),
                reason,
                status,
                new ReturnToken(returnToken),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentCode(assignedDepartment),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentCode(returnedDepartment),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId(assignedTo),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.UserId(processedBy),
                reasonCode,
                createdAt,
                updatedAt
        );

        return repository.save(entity);
    }

}
