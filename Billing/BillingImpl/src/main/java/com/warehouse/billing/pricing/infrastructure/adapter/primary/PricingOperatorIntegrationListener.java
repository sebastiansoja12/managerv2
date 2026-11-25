package com.warehouse.billing.pricing.infrastructure.adapter.primary;

import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import org.springframework.stereotype.Component;

@Component
public class PricingOperatorIntegrationListener {

    private final PricingPort pricingPort;

    public PricingOperatorIntegrationListener(final PricingPort pricingPort) {
        this.pricingPort = pricingPort;
    }

    @IntegrationEventListener(group = "manager-billing-pricing")
    public void handle(final OperatorCreatedIntegrationEvent event) {
        this.pricingPort.createDefaultPriceList(event.operatorId());
    }
}
