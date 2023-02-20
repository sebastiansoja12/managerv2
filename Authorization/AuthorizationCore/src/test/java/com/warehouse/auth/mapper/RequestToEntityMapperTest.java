package com.warehouse.auth.mapper;

import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RequestToEntityMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.RequestToEntityMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RequestToEntityMapperTest {


    private RequestToEntityMapper requestToEntityMapper;


    @BeforeEach
    void setup() {
        requestToEntityMapper = new RequestToEntityMapperImpl();
    }

    @Test
    void shouldMap() {
        // given
        final RegisterRequest request = new RegisterRequest();
        request.setDepotCode("KT1");
        request.setEmail("sebastian5152@wp.pl");
        request.setUsername("test");
        request.setPassword("test");
        request.setFirstName("test");
        request.setLastName("test");
        // when
        final UserEntity entity = requestToEntityMapper.map(request);
        // then
        assertAll(
                () -> assertThat(entity.getDepotCode()).isEqualTo(request.getDepotCode()),
                () -> assertThat(entity.getFirstName()).isEqualTo(request.getFirstName()),
                () -> assertThat(entity.getUsername()).isEqualTo(request.getUsername())
        );
    }
}
