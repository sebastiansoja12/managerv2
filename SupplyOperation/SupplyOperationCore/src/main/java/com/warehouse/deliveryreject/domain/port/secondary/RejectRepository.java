package com.warehouse.deliveryreject.domain.port.secondary;

import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity.RejectReasonEntity;

public interface RejectRepository {
    DeliveryReject create(final RejectReasonEntity rejectReason);
    RejectReason findRejectReason(final RejectReasonId rejectReasonId);
}
