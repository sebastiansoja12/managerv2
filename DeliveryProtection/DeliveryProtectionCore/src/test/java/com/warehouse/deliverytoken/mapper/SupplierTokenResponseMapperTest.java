package com.warehouse.deliverytoken.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import com.warehouse.commonassets.enumeration.ParcelType;
import org.junit.jupiter.api.Test;

import com.warehouse.deliverytoken.domain.vo.DeliveryPackageResponse;
import com.warehouse.deliverytoken.domain.vo.DeliveryTokenResponse;
import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ProtectedDelivery;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.DeliveryTokenResponseDto;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper.SupplierTokenResponseMapper;

public class SupplierTokenResponseMapperTest {
    
    private final SupplierTokenResponseMapper mapper = getMapper(SupplierTokenResponseMapper.class);
    
    @Test
    void shouldMapToDeliveryTokenResponseDto() {
        // given
        final DeliveryTokenResponse response = new DeliveryTokenResponse(
                createDeliveryPackageResponses(), "abc"
        );
        // when
        final DeliveryTokenResponseDto responseDto = mapper.map(response);
        // then
        assertEquals(response.getSupplierCode(), responseDto.getSupplierCode());
    }

	private List<DeliveryPackageResponse> createDeliveryPackageResponses() {
		final DeliveryPackageResponse response = new DeliveryPackageResponse(
				new Parcel(1L, null, ParcelType.PARENT, "KT1"),"1",
				new ProtectedDelivery("protectionToken", "9d51d7c2-6784-4a2c-b319-c406579c96f4"));
		return List.of(response);
	}

}
