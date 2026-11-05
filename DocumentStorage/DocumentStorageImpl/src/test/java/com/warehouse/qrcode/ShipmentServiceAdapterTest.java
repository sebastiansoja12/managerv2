package com.warehouse.qrcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.vo.LabelParty;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDimensionsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceAdapterTest {

    @Mock
    private ShipmentApiService shipmentApiService;

    @InjectMocks
    private ShipmentServiceAdapter adapter;

    @Test
    void shouldMapShipmentDocumentWithoutPersonalData() {
        final com.warehouse.commonassets.identificator.ShipmentId shipmentId =
                new com.warehouse.commonassets.identificator.ShipmentId(1L);
        final ShipmentPartyDto sender = new ShipmentPartyDto("Jan", "Kowalski", "jan@test.pl", "111",
                "Katowice", "40-000", "Mariacka 1", "PL");
        final ShipmentPartyDto recipient = new ShipmentPartyDto("Anna", "Nowak", "anna@test.pl", "222",
                "Krakow", "30-000", "Florianska 2", "PL");
        when(shipmentApiService.getShipmentDocument(shipmentId))
                .thenReturn(new ShipmentDocumentDto(shipmentId, null, null, "INP000000001", new BigDecimal("2.5"),
                        sender, recipient, ShipmentPriority.HIGH, "EXPRESS",
                        new ShipmentDimensionsDto(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ONE, "CM")));

        final Shipment shipment = adapter.find(new ShipmentId(1L));

        assertThat(shipment.shipmentId().getValue()).isEqualTo(1L);
        assertThat(shipment.trackingNumber().value()).isEqualTo("INP000000001");
        assertThat(shipment.sender()).isEqualTo(new LabelParty("J. K.", "Katowice", "40-000"));
        assertThat(shipment.recipient()).isEqualTo(new LabelParty("A. N.", "Krakow", "30-000"));
        assertThat(shipment.weightInKilograms()).isEqualByComparingTo("2.5");
        assertThat(shipment.dimensions().format()).isEqualTo("10 x 10 x 1 cm");
        assertThat(shipment.priority()).isEqualTo(ShipmentPriority.HIGH);
        assertThat(shipment.serviceLevel()).isEqualTo("EXPRESS");
        assertThat(shipment.toString()).doesNotContain("Kowalski", "Nowak", "jan@test.pl", "111", "Mariacka",
                "Florianska");
    }
}
