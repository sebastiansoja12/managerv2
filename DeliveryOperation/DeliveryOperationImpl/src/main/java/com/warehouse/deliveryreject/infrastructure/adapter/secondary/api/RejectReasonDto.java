package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.deliveryreject.domain.vo.RejectReason;
import org.apache.commons.lang3.StringUtils;

public record RejectReasonDto(String value) {
    public static RejectReasonDto from(final RejectReason rejectReason) {
        return new RejectReasonDto(rejectReason != null ? rejectReason.getValue() : StringUtils.EMPTY);
    }
}
