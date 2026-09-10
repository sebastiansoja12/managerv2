package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.*;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentOwnerContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository {

    void createOrUpdate(final Shipment shipment);

    Shipment findById(final ShipmentId shipmentId);

    boolean exists(final ShipmentId shipmentId);

    Optional<Shipment> findByExternalId(final ExternalId<String> externalId);

    Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId);

    Optional<Shipment> findByShipmentRelatedId(final ShipmentId shipmentRelatedId);

    Shipment findByTrackingNumber(final TrackingNumber trackingNumber);

    Optional<OperatorId> findOperatorIdByTrackingNumber(final TrackingNumber trackingNumber);

    List<Shipment> findAll();

    List<Shipment> findAllCreatedBetween(final LocalDateTime createdFromInclusive,
                                         final LocalDateTime createdToExclusive);

    boolean existsAny();

    ShipmentOwnerContext findContext(final TrackingNumber trackingNumber);
}
