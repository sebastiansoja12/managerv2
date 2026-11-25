package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.domain.model.LateChargeRefund;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.LateChargeRefundEntity;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.Optional;

public class LateChargeRefundRepositoryImpl implements LateChargeRefundRepository {

    private final OperatorFilteredRepository<LateChargeRefundEntity> repository;

    private final NoOperatorContextRepository<LateChargeRefundEntity> platformRepository;

    public LateChargeRefundRepositoryImpl(
            final OperatorFilteredRepository<LateChargeRefundEntity> repository,
            final NoOperatorContextRepository<LateChargeRefundEntity> platformRepository) {
        this.repository = repository;
        this.platformRepository = platformRepository;
    }

    @Override
    public boolean exists(final String gatewayReference) {
        return this.repository.createCriteria(LateChargeRefundEntity.class)
                .eq("gatewayReference", gatewayReference)
                .one()
                .isPresent();
    }

    @Override
    public boolean existsByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return this.repository.createCriteria(LateChargeRefundEntity.class)
                .eq("gatewayPaymentIntent", gatewayPaymentIntent)
                .one()
                .isPresent();
    }

    @Override
    public Optional<OperatorId> findOperatorByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return this.platformRepository.createCriteria(LateChargeRefundEntity.class)
                .eq("gatewayPaymentIntent", gatewayPaymentIntent)
                .one()
                .map(LateChargeRefundEntity::operatorId);
    }

    @Override
    public void save(final LateChargeRefund lateChargeRefund) {
        this.repository.create(new LateChargeRefundEntity(lateChargeRefund.gatewayReference(),
                lateChargeRefund.gatewayPaymentIntent(), lateChargeRefund.paymentId(),
                Money.from(lateChargeRefund.amount().getAmount(), lateChargeRefund.amount().getCurrency()),
                lateChargeRefund.refundedAt()));
    }
}
