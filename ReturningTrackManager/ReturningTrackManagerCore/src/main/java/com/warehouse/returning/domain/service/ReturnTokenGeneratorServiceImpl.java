package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;

public class ReturnTokenGeneratorServiceImpl implements ReturnTokenGeneratorService {
    @Override
    public ReturnToken generateToken(final ShipmentId shipmentId,
                                     final DepartmentCode departmentCode,
                                     final UserId userId) {

        final Long shipmentValue = shipmentId.value();
        final Long userValue = userId.value();
        final int departmentHash = departmentCode.value().hashCode();

        final long combined = shipmentValue * 31 + departmentHash * 17L + userValue * 13 + System.nanoTime();

        final long tokenNumber = Math.abs(combined) % 1_000_000;

        final String token = String.format("%06d", tokenNumber);

        return new ReturnToken(token);
    }
}
