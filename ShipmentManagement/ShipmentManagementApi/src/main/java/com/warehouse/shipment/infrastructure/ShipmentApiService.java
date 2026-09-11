package com.warehouse.shipment.infrastructure;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentHandoverRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRerouteDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentReturnResponseDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingDto;
import com.warehouse.shipment.infrastructure.dto.SourceShipmentDto;

public interface ShipmentApiService {
    ShipmentDetailsDto getShipment(final ShipmentId shipmentId);

    ShipmentDocumentDto getShipmentDocument(final ShipmentId shipmentId);

    ShipmentTrackingDto getShipmentTracking(final TrackingNumber trackingNumber);

    ShipmentDetailsDto collectShipment(final TrackingNumber trackingNumber);

    ShipmentDetailsDto handOverShipment(final ShipmentHandoverRequestDto shipmentHandoverRequest);

    ShipmentDetailsDto loadShipmentOnPallet(final ShipmentId shipmentId);

    Optional<ShipmentRerouteDetailsDto> getShipmentForReroute(final TrackingNumber trackingNumber);

    Optional<SourceShipmentDto> findSourceShipment(final ShipmentId shipmentId);

    ShipmentRejectResponseDto rejectShipment(final ShipmentRejectRequestDto shipmentRejectRequest);

    void markReturned(final ShipmentId shipmentId);

    // Ends the shipment's lifecycle as returned and creates the return shipment (recipient -> sender).
    ShipmentReturnResponseDto returnShipment(final ShipmentId shipmentId);

    void markDelivered(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void completeReturn(final ShipmentId shipmentId);
    void cancelReturnShipment(final ShipmentId shipmentId);

    boolean hasAnyShipment();
}
