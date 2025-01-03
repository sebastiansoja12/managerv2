package com.warehouse.deliveryreject.domain.service;

import com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity.RejectReasonEntity;
import org.springframework.stereotype.Service;

import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.port.secondary.RejectRepository;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;

@Service
public class RejectServiceImpl implements RejectService {

    private final RejectRepository rejectRepository;

    public RejectServiceImpl(final RejectRepository rejectRepository) {
        this.rejectRepository = rejectRepository;
    }

    @Override
    public RejectReason findRejectReason(final RejectReasonId rejectReasonId) {
        return rejectRepository.findRejectReason(rejectReasonId);
    }

    @Override
    public DeliveryReject createReject(final DeliveryReject deliveryReject) {
        final RejectReasonEntity rejectReason = RejectReasonEntity.from(deliveryReject);
        return rejectRepository.create(rejectReason);
    }
}
