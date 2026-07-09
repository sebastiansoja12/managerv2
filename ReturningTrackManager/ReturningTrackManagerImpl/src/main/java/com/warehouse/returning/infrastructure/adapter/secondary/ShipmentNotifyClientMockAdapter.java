package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.domain.enumeration.ErrorCode;
import com.warehouse.returning.domain.enumeration.ResponseStatus;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.port.secondary.ShipmentNotifyClientPort;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

public class ShipmentNotifyClientMockAdapter implements ShipmentNotifyClientPort {
    @Override
    public Result<ResponseStatus, ErrorCode> notifyReturnChanged(final ReturnPackageSnapshot snapshot) {
        return Result.success(ResponseStatus.OK);
    }
}
