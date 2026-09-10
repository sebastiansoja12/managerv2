package com.warehouse.csv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.csv.domain.vo.ParcelCsv;
import com.warehouse.csv.infrastructure.adapter.secondary.ShipmentServiceAdapter;
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
        final ShipmentId shipmentId = new ShipmentId(1L);
        final ShipmentPartyDto sender = new ShipmentPartyDto("Jan", "Kowalski", "jan@test.pl", "111",
                "Katowice", "40-000", "Mariacka 1", "PL");
        final ShipmentPartyDto recipient = new ShipmentPartyDto("Anna", "Nowak", "anna@test.pl", "222",
                "Krakow", "30-000", "Florianska 2", "PL");
        when(shipmentApiService.getShipmentDocument(shipmentId))
                .thenReturn(new ShipmentDocumentDto(shipmentId, null, null, null, null, sender, recipient));

        final ParcelCsv parcel = adapter.find(1L);

        assertThat(parcel.getShipmentId()).isEqualTo(1L);
        assertThat(parcel.getFirstName()).isEqualTo("Jan");
        assertThat(parcel.getSenderTelephoneNumber()).isEqualTo("111");
        assertThat(parcel.getRecipientLastName()).isEqualTo("Nowak");
        assertThat(parcel.getRecipientTelephoneNumber()).isEqualTo("222");
        assertThat(parcel.getRecipientStreet()).isEqualTo("Florianska 2");
    }
}
