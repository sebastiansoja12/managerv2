package com.warehouse.returning.infrastructure.adapter.secondary.api;

import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.enumeration.ReasonCode;

public record ChangeReasonCodeRequest(ReturnPackageId returnPackageId, ReasonCode reasonCode) {
}
