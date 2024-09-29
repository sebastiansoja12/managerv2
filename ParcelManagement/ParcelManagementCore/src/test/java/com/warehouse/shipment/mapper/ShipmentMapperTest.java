package com.warehouse.shipment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ShipmentSize;
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
                .parcel(createParcel())
                .build();
        // when
        final Parcel parcel = mapper.map(request);
        // then
        // we dont map ID
        assertThat(parcel.getId()).isEqualTo(null);
        assertThat(parcel.getSender().getCity()).isEqualTo("Test");
    }


    private ShipmentParcel createParcel() {
        return ShipmentParcel.builder()
                .parcelSize(ShipmentSize.TEST)
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
