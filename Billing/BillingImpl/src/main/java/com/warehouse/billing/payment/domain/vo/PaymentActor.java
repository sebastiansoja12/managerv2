package com.warehouse.billing.payment.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

public record PaymentActor(UserId userId, DepartmentId departmentId) {
}
