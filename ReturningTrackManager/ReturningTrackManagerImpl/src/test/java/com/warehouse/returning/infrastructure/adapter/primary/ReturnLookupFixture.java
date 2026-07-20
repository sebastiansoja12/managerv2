package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.*;

final class ReturnLookupFixture {
    static ReturnPackage cancelledReturn() {
        final ReturnPackage returnPackage = new ReturnPackage(
                new ReturnPackageId(9223372036854775001L), new ShipmentId(6805406359141427429L),
                "Damaged parcel", new ReturnToken("RETURN-123"), new DepartmentId(1L),
                new DepartmentId(2L), new UserId(1L), new UserId(2L), ReasonCode.DAMAGED, new OperatorId(7L));
        returnPackage.markAsCanceled();
        return returnPackage;
    }
}
