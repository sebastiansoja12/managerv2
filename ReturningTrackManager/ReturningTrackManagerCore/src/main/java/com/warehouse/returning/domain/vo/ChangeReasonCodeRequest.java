package com.warehouse.returning.domain.vo;

import com.warehouse.returning.domain.enumeration.ReasonCode;

public record ChangeReasonCodeRequest(ReturnPackageId returnPackageId, ReasonCode reasonCode) {
}
