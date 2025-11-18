package com.warehouse.supplier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.supplier.domain.model.SupplierCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.supplier.dto.SupplierAddRequestDto;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierRequestMapper;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierRequestMapperImpl;

public class SupplyRequestMapperTest {

    private SupplierRequestMapper requestMapper;

    @BeforeEach
    void setup() {
        requestMapper = new SupplierRequestMapperImpl();
    }



    @Test
    void shouldMapFromRequestToSupplier() {
        // given
        final SupplierAddRequestDto requestDto = SupplierAddRequestDto.builder()
                .firstName("test")
                .build();
        // when
        final SupplierCreateRequest request1 = requestMapper.map(requestDto);
        // then
        assertThat(request1.getFirstName()).isEqualTo(requestDto.getFirstName());
    }

    @Test
    void shouldMapFromRequestListToSupplierList() {
        // given
        final SupplierAddRequestDto request = SupplierAddRequestDto.builder()
                .firstName("test")
                .build();
        final List<SupplierAddRequestDto> requests = new ArrayList<>();
        requests.add(request);
        // when
        final List<SupplierCreateRequest> suppliers = requestMapper.map(requests);
        // then
        assertThat(suppliers.get(0).getFirstName()).isEqualTo(requests.get(0).getFirstName());
    }
}
