package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.application.service.ReturnProcessingFixture;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.domain.vo.ShipmentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReturnReadModelMapperTest {

    @Test
    void shouldMapReturnSnapshotToReadModelAndBack() {
        final ReturnPackageSnapshot snapshot = withReturnShipment(
                ReturnProcessingFixture.returning(ReturnStatus.PROCESSING).toSnapshot(), new ShipmentId(789L));

        final ReturnPackage mapped = ReturnReadModelMapper.map(ReturnReadModelMapper.map(snapshot));

        assertAll(
                () -> assertEquals(snapshot.returnPackageId(), mapped.getReturnPackageId()),
                () -> assertEquals(snapshot.shipmentId(), mapped.getShipmentId()),
                () -> assertEquals(new ShipmentId(789L), mapped.getReturnShipmentId()),
                () -> assertEquals(ReturnStatus.PROCESSING, mapped.getReturnStatus()),
                () -> assertEquals(snapshot.assignedDepartmentId(), mapped.getAssignedDepartmentId()),
                () -> assertEquals(snapshot.operatorId(), mapped.getOperatorId()),
                () -> assertEquals(snapshot.reasonCode(), mapped.getReasonCode()),
                () -> assertEquals(snapshot.updatedAt(), mapped.getUpdatedAt())
        );
    }

    private ReturnPackageSnapshot withReturnShipment(final ReturnPackageSnapshot snapshot,
                                                     final ShipmentId returnShipmentId) {
        return new ReturnPackageSnapshot(snapshot.returnPackageId(), snapshot.shipmentId(), returnShipmentId,
                snapshot.reason(), snapshot.returnStatus(), snapshot.returnToken(), snapshot.assignedDepartmentId(),
                snapshot.returnedDepartmentId(), snapshot.assignedTo(), snapshot.processedBy(), snapshot.reasonCode(),
                snapshot.operatorId(), snapshot.createdAt(), snapshot.updatedAt());
    }
}
