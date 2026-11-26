package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.domain.model.LateChargeRefund;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryLateChargeRefundRepository implements LateChargeRefundRepository {

    private final Map<String, LateChargeRefund> refunds = new LinkedHashMap<>();

    @Override
    public boolean exists(final String gatewayReference) {
        return this.refunds.containsKey(gatewayReference);
    }

    @Override
    public boolean existsByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return this.refunds.values().stream()
                .anyMatch(refund -> gatewayPaymentIntent.equals(refund.gatewayPaymentIntent()));
    }

    @Override
    public Optional<OperatorId> findOperatorByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return existsByGatewayPaymentIntent(gatewayPaymentIntent) ? Optional.of(OperatorId.of(10L)) : Optional.empty();
    }

    @Override
    public void save(final LateChargeRefund lateChargeRefund) {
        this.refunds.put(lateChargeRefund.gatewayReference(), lateChargeRefund);
    }

    List<LateChargeRefund> all() {
        return List.copyOf(this.refunds.values());
    }
}
