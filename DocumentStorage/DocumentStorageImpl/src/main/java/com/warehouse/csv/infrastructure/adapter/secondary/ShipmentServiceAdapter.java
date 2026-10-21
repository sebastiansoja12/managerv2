package com.warehouse.csv.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.csv.domain.port.secondary.ShipmentServicePort;
import com.warehouse.csv.domain.vo.ParcelCsv;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public ParcelCsv find(final Long shipmentId) {
        final ShipmentDocumentDto document = shipmentApiService.getShipmentDocument(new ShipmentId(shipmentId));
        final ParcelCsv.ParcelCsvBuilder builder = ParcelCsv.builder()
                .shipmentId(document.shipmentId().getValue());
        final ShipmentPartyDto sender = document.sender();
        if (sender != null) {
            builder.firstName(sender.firstName())
                    .lastName(sender.lastName())
                    .senderEmail(sender.email())
                    .senderTelephoneNumber(sender.telephoneNumber())
                    .senderCity(sender.city())
                    .senderPostalCode(sender.postalCode())
                    .senderStreet(sender.street());
        }
        final ShipmentPartyDto recipient = document.recipient();
        if (recipient != null) {
            builder.recipientFirstName(recipient.firstName())
                    .recipientLastName(recipient.lastName())
                    .recipientEmail(recipient.email())
                    .recipientTelephoneNumber(recipient.telephoneNumber())
                    .recipientCity(recipient.city())
                    .recipientPostalCode(recipient.postalCode())
                    .recipientStreet(recipient.street());
        }
        return builder.build();
    }
}
