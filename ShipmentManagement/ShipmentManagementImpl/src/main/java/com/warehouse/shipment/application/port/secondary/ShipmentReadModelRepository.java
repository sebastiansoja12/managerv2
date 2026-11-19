package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import java.util.Optional;

import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentReadModelRepository {

    void sync(final ShipmentSnapshot snapshot);

    void sync(final ShipmentSnapshot snapshot, final ShipmentDeliveryDetails deliveryDetails);

    Optional<Shipment> findById(final ShipmentId shipmentId);

    Optional<ShipmentDeliveryDetails> findDeliveryDetails(final ShipmentId shipmentId);

    void changeIncidentReported(final ShipmentId shipmentId, final boolean incidentReported);

    boolean isIncidentReported(final ShipmentId shipmentId);

    void changePalletTrackingNumber(final ShipmentId shipmentId, final PalletTrackingNumber palletTrackingNumber);

    Optional<PalletTrackingNumber> findPalletTrackingNumber(final ShipmentId shipmentId);

    boolean exists(final ShipmentId shipmentId);

    Optional<Shipment> findByExternalId(final ExternalId<String> externalId);

    Optional<ShipmentId> findIdByExternalId(final ExternalId<String> externalId);

    Optional<Shipment> findByTrackingNumber(final TrackingNumber trackingNumber);
}
