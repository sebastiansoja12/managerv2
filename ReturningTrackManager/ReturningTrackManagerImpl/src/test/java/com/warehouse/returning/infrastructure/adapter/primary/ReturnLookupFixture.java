package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.*;

final class ReturnLookupFixture {
    static ReturnPackage cancelledReturn() {
        final ReturnPackage returnPackage = new ReturnPackage(
                new ReturnPackageId(9223372036854775001L), new ShipmentId(6805406359141427429L),
                "Damaged parcel", new ReturnToken("RETURN-123"), new DepartmentCode("KT1"),
                new DepartmentCode("KT2"), new UserId(1L), new UserId(2L), ReasonCode.DAMAGED, 7L);
        returnPackage.markAsCanceled();
        return returnPackage;
    }
}
