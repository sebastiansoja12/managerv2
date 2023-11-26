package com.warehouse.shipment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapperImpl;

public class ParcelMapperTest {

    private ParcelMapper mapper;


    @BeforeEach
    void setup() {
        mapper = new ParcelMapperImpl();
    }

    @Test
    void shouldMapFromParcelToEntity() {
        // given
        final ShipmentParcel parcel = createParcel();

        // when
        final ParcelEntity parcelEntity = mapper.map(parcel);

        // then
        assertThat(parcelEntity.getParcelSize().getSize()).isEqualTo("test");
        // and status is enum type CREATED
        assertThat(parcelEntity.getStatus().name()).isEqualTo("CREATED");

    }

    @Test
    void shouldMapFromEntityToParcel() {
        // given
        final ParcelEntity parcelEntity = ParcelEntity.builder()
                .parcelSize(Size.TEST)
                .lastName("test")
                .status(Status.CREATED)
                .build();
        // when
        final Parcel parcel = mapper.map(parcelEntity);

        // then
        assertThat(parcel.getPrice()).isEqualTo(99);
        assertThat(parcel.getSender().getLastName()).isEqualTo("test");

        // and status is enum type CREATED
        assertThat(parcel.getParcelStatus()).isEqualTo(Status.CREATED);
    }
    private ShipmentParcel createParcel() {
        return ShipmentParcel.builder()
                .parcelSize(Size.TEST)
                .price(20)
                .destination("KT1")
                .status(Status.CREATED)
                .sender(null)
                .recipient(null)
                .build();
    }

}
