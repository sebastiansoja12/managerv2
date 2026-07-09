package com.warehouse.shipment.domain.generator;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.commonassets.identificator.TrackingNumber;

public interface TrackingNumberGenerator {

    boolean canHandle(CarrierOperator carrier);

    TrackingNumber generate();
}

