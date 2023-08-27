package com.warehouse.supplier.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.dto.SupplierDto;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierResponseMapper;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierResponseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryResponseMapperTest {

    private SupplierResponseMapper responseMapper;

    @BeforeEach
    void setup() {
        responseMapper = new SupplierResponseMapperImpl();
    }

    @Test
    void shouldMapFromSupplierToDto() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setFirstName("test");
        // when
        final SupplierDto supplierDto = responseMapper.map(supplier);
        // then
        assertThat(supplierDto.getFirstName()).isEqualTo(supplier.getFirstName());
    }

    @Test
    void shouldMapFromListToDtoList() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setFirstName("test");
        final List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(supplier);
        // when
        final List<SupplierDto> supplierDtos = responseMapper.map(suppliers);
        // then
        assertThat(suppliers.get(0).getFirstName()).isEqualTo(supplierDtos.get(0).getFirstName());
    }
}
