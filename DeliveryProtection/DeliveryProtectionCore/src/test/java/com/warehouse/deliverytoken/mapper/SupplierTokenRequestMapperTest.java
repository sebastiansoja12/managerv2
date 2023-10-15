package com.warehouse.deliverytoken.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.deliverytoken.domain.model.DeliveryPackageRequest;
import com.warehouse.deliverytoken.domain.model.DeliveryTokenRequest;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.dto.*;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.mapper.SupplierTokenRequestMapper;

public class SupplierTokenRequestMapperTest {

    private final SupplierTokenRequestMapper mapper = getMapper(SupplierTokenRequestMapper.class);


    @Test
    void shouldMap() {
        // given
        final DeliveryPackageRequestDto request = createDeliveryPackageRequestDto();
        // when
        final DeliveryPackageRequest deliveryPackageRequest = mapper.map(request);
        // then
        assertEquals(request.getSupplier().getSupplierCode(), deliveryPackageRequest.getSupplier().getSupplierCode());
        assertEquals(request.getDelivery().getId(), deliveryPackageRequest.getDelivery().getId());
        assertEquals(request.getParcelId().getValue(), deliveryPackageRequest.getParcel().getId());
    }

    @Test
    void shouldMapDeliveryTokenRequest() {
        // given
        final DeliveryTokenRequestDto request = new DeliveryTokenRequestDto(
                List.of(createDeliveryPackageRequestDto()));
        // when
        final DeliveryTokenRequest deliveryTokenRequest = mapper.map(request);
        // then
        assertEquals(request.getDeliveryPackageRequests().get(0).getDelivery().getId(),
                deliveryTokenRequest.getDeliveryPackageRequests().get(0).getDelivery().getId());
    }

    private DeliveryPackageRequestDto createDeliveryPackageRequestDto() {
        return new DeliveryPackageRequestDto(
                new DeliveryDto("1"), new SupplierDto("abc"), new ParcelIdDto(1L)
        );
    }
}
