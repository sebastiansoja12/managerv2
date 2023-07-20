package com.warehouse.shipment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.paypal.domain.model.LinkInformation;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.Sender;
import com.warehouse.shipment.domain.model.ShipmentRequest;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapperImpl;

public class ShipmentMapperTest {

    private ShipmentMapper mapper;


    @BeforeEach
    void setup() {
        mapper = new ShipmentMapperImpl();
    }

    @Test
    void shouldMapFromRequestToParcel() {
        // given
        final ShipmentRequest request = ShipmentRequest.builder()
                //.parcel(createParcel())
                .build();
        // when
        final Parcel parcel = mapper.map(request);
        // then
        // we dont map ID
        assertThat(parcel.getId()).isEqualTo(null);
        assertThat(parcel.getSender().getCity()).isEqualTo("Test");
    }



    private LinkInformation createLink() {
        final LinkInformation link = new LinkInformation();
        link.setPaymentUrl("test.pl");
        return link;
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .parcelSize(Size.TEST)
                .id(1L)
                .price(20)
                .sender(createSender())
                .recipient(null)
                .build();
    }

    private Sender createSender() {
        return Sender.builder()
                .city("Test")
                .build();
    }
}
