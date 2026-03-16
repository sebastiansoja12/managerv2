package com.warehouse.supplier.infrastructure.adapter.secondary;

import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import com.warehouse.supplier.infrastructure.adapter.secondary.api.RegisterRequestDto;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.OutputRequestMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceAdapter implements UserServicePort {

    private final RestClient restClient;

    public UserServiceAdapter() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    @Override
    public UserId createUserForSupplier(final SupplierSnapshot snapshot) {
        final RegisterRequestDto registerRequest = OutputRequestMapper.map(snapshot);
        return null;
    }
}
