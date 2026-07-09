package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.enumeration.ErrorCode;
import com.warehouse.returning.domain.enumeration.ResponseStatus;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

public interface ShipmentNotifyClientPort {
    Result<ResponseStatus, ErrorCode> notifyReturnChanged(final ReturnPackageSnapshot snapshot);
}
