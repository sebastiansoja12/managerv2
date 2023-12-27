package com.warehouse.supplier.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.SupplierAddResponse;
import com.warehouse.supplier.dto.SupplierAddResponseDto;
import com.warehouse.supplier.dto.SupplierDto;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierResponseMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupplyResponseMapperTest {

    private final SupplierResponseMapper mapper = Mappers.getMapper(SupplierResponseMapper.class);


    @Test
    void shouldMapFromSupplierToDto() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setSupplierCode("code");
        // when
        final SupplierDto supplierDto = mapper.map(supplier);
        // then
        assertEquals(supplierDto.getSupplierCode(), supplier.getSupplierCode());
    }

    @Test
    void shouldMapFromSupplierListToDtoList() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setSupplierCode("code");
        // when
        final List<SupplierDto> supplierDto = mapper.mapToDto(Collections.singletonList(supplier));
        // then
        assertEquals(supplierDto.get(0).getSupplierCode(), supplier.getSupplierCode());
    }

    @Test
    void shouldMapRequestToDto() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setSupplierCode("code");
        final SupplierAddResponse response = new SupplierAddResponse(supplier);
        // when
        final SupplierAddResponseDto responseDto = mapper.map(response);
        // then
        assertEquals(supplier.getSupplierCode(), responseDto.getSupplierCode());
    }

    @Test
    void shouldMapRequestListToDtoList() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setSupplierCode("code");
        final SupplierAddResponse response = new SupplierAddResponse(supplier);
        // when
        final List<SupplierAddResponseDto> responseDto = mapper.map(Collections.singletonList(response));
        // then
        assertEquals(supplier.getSupplierCode(), responseDto.get(0).getSupplierCode());
    }
}
