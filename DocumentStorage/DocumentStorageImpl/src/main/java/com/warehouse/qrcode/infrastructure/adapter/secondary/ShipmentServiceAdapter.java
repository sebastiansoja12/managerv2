package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.vo.LabelParty;
import com.warehouse.qrcode.domain.vo.ParcelDimensions;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDimensionsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public Shipment find(final ShipmentId shipmentId) {
        final ShipmentDocumentDto document = shipmentApiService.getShipmentDocument(
                new com.warehouse.commonassets.identificator.ShipmentId(shipmentId.getValue()));
        return new Shipment(
                new ShipmentId(document.shipmentId().getValue()),
                document.trackingNumber() == null ? null : new TrackingNumber(document.trackingNumber()),
                party(document.sender()),
                party(document.recipient()),
                document.weightInKilograms(),
                dimensions(document.dimensions()),
                document.priority(),
                document.serviceLevel());
    }

    private LabelParty party(final ShipmentPartyDto party) {
        if (party == null) {
            return LabelParty.unknown();
        }
        return LabelParty.of(party.firstName(), party.lastName(), party.city(), party.postalCode());
    }

    private ParcelDimensions dimensions(final ShipmentDimensionsDto dimensions) {
        if (dimensions == null) {
            return null;
        }
        return new ParcelDimensions(dimensions.length(), dimensions.width(), dimensions.height(), dimensions.unit());
    }
}
