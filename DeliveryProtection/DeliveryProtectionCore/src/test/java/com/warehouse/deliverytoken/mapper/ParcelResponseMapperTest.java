package com.warehouse.deliverytoken.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.commonassets.enumeration.ShipmentType;
import org.junit.jupiter.api.Test;

import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.dto.ParcelIdDto;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.enumeration.ParcelTypeDto;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.mapper.ParcelResponseMapper;

public class ParcelResponseMapperTest {
    
    
	private final ParcelResponseMapper mapper = getMapper(ParcelResponseMapper.class);

	@Test
	void shouldMap() {
		// given
        final ParcelDto parcelDto = new ParcelDto(new ParcelIdDto(1L), ParcelTypeDto.PARENT, null, "KT1");
		// when
        final Parcel parcel = mapper.map(parcelDto);
		// then
        assertThat(parcel)
                .extracting(
                        Parcel::getParcelRelatedId,
                        Parcel::getDestination
                )
                .containsExactly(
                        parcelDto.getParcelRelatedId(),
                        parcelDto.getDestination()
                );
	}

    @Test
    void shouldMapToParcelType() {
        // given
        final ParcelTypeDto parcelTypeDto = ParcelTypeDto.CHILD;
        // when
        final ShipmentType shipmentType = mapper.mapToParcelType(parcelTypeDto);
        // then
        assertEquals(parcelTypeDto.name(), shipmentType.name());
    }
}
