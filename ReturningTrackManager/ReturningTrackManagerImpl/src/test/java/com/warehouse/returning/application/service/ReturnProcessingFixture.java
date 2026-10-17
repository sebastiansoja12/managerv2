package com.warehouse.returning.application.service;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.*;
import java.time.Instant;

public final class ReturnProcessingFixture {
    private ReturnProcessingFixture() {
    }

    public static ReturnPackage returning(final ReturnStatus status) {
        return new ReturnPackage(new ReturnPackageId(123L), new ShipmentId(456L), "Damaged parcel",
                status, new ReturnToken("123456"), new DepartmentId(3L), null,
                new UserId(11L), new UserId(12L), ReasonCode.DAMAGED, new OperatorId(7L),
                Instant.parse("2026-09-01T12:00:00Z"), Instant.parse("2026-09-01T12:00:00Z"));
    }
}
