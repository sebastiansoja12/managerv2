package com.warehouse.billing.pricing.domain.service;

import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceListId;

import java.time.Instant;

public class PriceListFactory {

    public PriceList copyForOperator(final PriceList template, final OperatorId operatorId) {
        return new PriceList(PriceListId.nextId(), operatorId, template.currency(), template.sizeClasses(),
                template.weightBands(), template.serviceLevelMultipliers(), template.deliveryOptionSurcharges(),
                template.insurancePercent(), template.vatPercent(), Instant.now());
    }
}
