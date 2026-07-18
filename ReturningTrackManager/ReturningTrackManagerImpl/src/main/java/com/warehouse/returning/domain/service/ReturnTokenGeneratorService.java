package com.warehouse.returning.domain.service;

import com.warehouse.common.DepartmentId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;

public interface ReturnTokenGeneratorService {
    ReturnToken generateToken(final ShipmentId shipmentId, final DepartmentId departmentId, final UserId userId);
}
