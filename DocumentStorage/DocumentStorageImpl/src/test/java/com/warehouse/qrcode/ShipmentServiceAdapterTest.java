package com.warehouse.qrcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceAdapterTest {

    @Mock
    private ShipmentApiService shipmentApiService;

    @InjectMocks
    private ShipmentServiceAdapter adapter;

    @Test
    void shouldMapShipmentDocument() {
        final com.warehouse.commonassets.identificator.ShipmentId shipmentId =
                new com.warehouse.commonassets.identificator.ShipmentId(1L);
        final ShipmentPartyDto sender = new ShipmentPartyDto("Jan", "Kowalski", "jan@test.pl", "111",
                "Katowice", "40-000", "Mariacka 1", "PL");
        final ShipmentPartyDto recipient = new ShipmentPartyDto("Anna", "Nowak", "anna@test.pl", "222",
                "Krakow", "30-000", "Florianska 2", "PL");
        when(shipmentApiService.getShipmentDocument(shipmentId))
                .thenReturn(new ShipmentDocumentDto(shipmentId, null, null, "INP000000001", null, sender, recipient));

        final Shipment shipment = adapter.find(new ShipmentId(1L));

        assertThat(shipment.getId()).isEqualTo(1L);
        assertThat(shipment.getTrackingNumber()).isEqualTo("INP000000001");
        assertThat(shipment.getFirstName()).isEqualTo("Jan");
        assertThat(shipment.getSenderTelephone()).isEqualTo("111");
        assertThat(shipment.getSenderStreet()).isEqualTo("Mariacka 1");
        assertThat(shipment.getRecipientLastName()).isEqualTo("Nowak");
        assertThat(shipment.getRecipientCity()).isEqualTo("Krakow");
        assertThat(shipment.getRecipientEmail()).isEqualTo("anna@test.pl");
    }
}
