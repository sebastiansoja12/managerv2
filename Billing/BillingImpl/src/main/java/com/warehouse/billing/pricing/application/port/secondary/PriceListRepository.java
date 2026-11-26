package com.warehouse.billing.pricing.application.port.secondary;

import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Optional;

public interface PriceListRepository {

    void create(final PriceList priceList);

    void update(final PriceList priceList);

    Optional<PriceList> findCurrent();

    Optional<PriceList> findByOperator(final OperatorId operatorId);

    Optional<PriceList> findTemplate();
}
