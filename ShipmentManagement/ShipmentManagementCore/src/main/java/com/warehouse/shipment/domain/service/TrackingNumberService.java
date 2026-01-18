package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.vo.TrackingNumber;

public interface TrackingNumberService {
    TrackingNumber nextTrackingNumber(final CarrierOperator carrierOperator);
}
