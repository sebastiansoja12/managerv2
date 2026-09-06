package com.warehouse.shipment.infrastructure;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRerouteDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;
import com.warehouse.shipment.infrastructure.dto.SourceShipmentDto;

public interface ShipmentApiService {
    ShipmentDetailsDto getShipment(final ShipmentId shipmentId);

    ShipmentDetailsDto collectShipment(final TrackingNumber trackingNumber);

    ShipmentDetailsDto loadShipmentOnPallet(final ShipmentId shipmentId);

    Optional<ShipmentRerouteDetailsDto> getShipmentForReroute(final TrackingNumber trackingNumber);

    Optional<SourceShipmentDto> findSourceShipment(final ShipmentId shipmentId);

    ShipmentRejectResponseDto rejectShipment(final ShipmentRejectRequestDto shipmentRejectRequest);

    void markReturned(final ShipmentId shipmentId);

    void markDelivered(final ShipmentId shipmentId);

    void restoreAfterReturnCancellation(final ShipmentId shipmentId);

    void completeReturn(final ShipmentId shipmentId);
    void cancelReturnShipment(final ShipmentId shipmentId);

    boolean hasAnyShipment();
}
