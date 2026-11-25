package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.billing.payment.domain.model.LateChargeRefund;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Optional;

public interface LateChargeRefundRepository {

    boolean exists(final String gatewayReference);

    boolean existsByGatewayPaymentIntent(final String gatewayPaymentIntent);

    Optional<OperatorId> findOperatorByGatewayPaymentIntent(final String gatewayPaymentIntent);

    void save(final LateChargeRefund lateChargeRefund);
}
