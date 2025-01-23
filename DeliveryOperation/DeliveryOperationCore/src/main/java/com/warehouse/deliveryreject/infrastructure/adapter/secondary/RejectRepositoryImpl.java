package com.warehouse.deliveryreject.infrastructure.adapter.secondary;

import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.port.secondary.RejectRepository;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity.RejectReasonEntity;

public class RejectRepositoryImpl implements RejectRepository {

    private final RejectReasonReadRepository rejectReasonReadRepository;

    public RejectRepositoryImpl(final RejectReasonReadRepository rejectReasonReadRepository) {
        this.rejectReasonReadRepository = rejectReasonReadRepository;
    }

    @Override
    public DeliveryReject create(final RejectReasonEntity rejectReason) {
        this.rejectReasonReadRepository.save(rejectReason);
        return DeliveryReject.from(rejectReason);
    }

    @Override
    public RejectReason findRejectReason(final RejectReasonId rejectReasonId) {
        return this.rejectReasonReadRepository
                .findById(rejectReasonId.value())
                .map(value -> new RejectReason(value.getReason()))
                .orElse(null);
    }
}
