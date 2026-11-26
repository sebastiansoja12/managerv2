package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ShipmentServicePort {

    Optional<SourceShipment> findSourceShipment(final ShipmentId shipmentId);

    Optional<ShipmentId> findDerivedShipment(final ShipmentId shipmentId);

    Optional<RecipientAddress> findRecipientAddress(final ShipmentId shipmentId);

    Map<ShipmentId, String> findRecipientPostalCodes(final Collection<ShipmentId> shipmentIds);

    boolean isCanceled(final ShipmentId shipmentId);

    void markDelivered(final ShipmentId shipmentId);
}
