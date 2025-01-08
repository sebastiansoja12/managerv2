package com.warehouse.deliveryreject.domain.service;

import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;


public interface RejectService {
    RejectReason findRejectReason(final RejectReasonId rejectReasonId);
    void createReject(final DeliveryReject deliveryReject);
}
