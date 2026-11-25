package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.PaymentDeadlineServicePort;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;

import java.time.Duration;

public class PaymentDeadlineServiceAdapter implements PaymentDeadlineServicePort {

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    public PaymentDeadlineServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public Duration paymentDeadline() {
        return Duration.ofHours(this.operatorConfigurationApiService.getCurrentShipmentConfiguration()
                .paymentConfiguration().paymentDeadlineHours());
    }
}
