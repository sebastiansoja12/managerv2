package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;

public interface TrackingNumberService {

    TrackingNumber nextTrackingNumber(final ShipmentId shipmentId);

    TrackingNumber nextTrackingNumber(final TrackingNumberRule rule);

    TrackingNumber nextTrackingNumber(final TrackingNumberRule rule, final ShipmentId shipmentId);
}
