package com.warehouse.shipment.mapper;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.domain.enumeration.Status;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ParcelMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParcelMapperTest {

    private ParcelMapper mapper;


    @BeforeEach
    void setup() {
        mapper = new ParcelMapperImpl();
    }

    @Test
    void shouldMapFromParcelToEntity() {
        // given
        final Parcel parcel = createParcel();

        // when
        final ParcelEntity parcelEntity = mapper.map(parcel);

        // then
        assertThat(parcelEntity.getParcelType().getSize()).isEqualTo("test");
        // and status is enum type CREATED
        assertThat(parcelEntity.getStatus().name()).isEqualTo("CREATED");

    }

    @Test
    void shouldMapFromEntityToParcel() {
        // given
        final ParcelEntity parcelEntity = ParcelEntity.builder()
                .price(20)
                .lastName("test")
                .status(Status.CREATED)
                .build();
        // when
        final Parcel parcel = mapper.map(parcelEntity);

        // then
        assertThat(parcel.getPrice()).isEqualTo(20);
        assertThat(parcel.getSender().getLastName()).isEqualTo("test");

        // and status is enum type CREATED
        assertThat(parcel.getStatus()).isEqualTo("CREATED");
    }
    private Parcel createParcel() {
        return Parcel.builder()
                .parcelType(ParcelType.TEST)
                .id(1L)
                .price(20)
                .destination("KT1")
                .status("CREATED")
                .sender(null)
                .recipient(null)
                .build();
    }

}
