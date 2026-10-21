package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
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
        final Shipment.ShipmentBuilder builder = Shipment.builder()
                .id(document.shipmentId().getValue())
                .trackingNumber(document.trackingNumber());
        final ShipmentPartyDto sender = document.sender();
        if (sender != null) {
            builder.firstName(sender.firstName())
                    .lastName(sender.lastName())
                    .senderTelephone(sender.telephoneNumber())
                    .senderEmail(sender.email())
                    .senderCity(sender.city())
                    .senderStreet(sender.street())
                    .senderPostalCode(sender.postalCode());
        }
        final ShipmentPartyDto recipient = document.recipient();
        if (recipient != null) {
            builder.recipientFirstName(recipient.firstName())
                    .recipientLastName(recipient.lastName())
                    .recipientTelephone(recipient.telephoneNumber())
                    .recipientEmail(recipient.email())
                    .recipientCity(recipient.city())
                    .recipientStreet(recipient.street())
                    .recipientPostalCode(recipient.postalCode());
        }
        return builder.build();
    }
}
