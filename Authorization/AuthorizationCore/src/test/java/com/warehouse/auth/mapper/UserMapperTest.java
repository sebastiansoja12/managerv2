package com.warehouse.auth.mapper;

import com.warehouse.auth.domain.model.Depot;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void shouldMapFromUserToUserEntity() {
        // given
        final Depot depot = Depot.builder()
                .city("test")
                .depotCode("TS1")
                .country("Poland")
                .street("test")
                .build();

        final User user = User.builder()
                .depot(depot)
                .email("test@test.pl")
                .firstName("test")
                .lastName("test")
                .password("test")
                .role("admin")
                .username("test")
                .build();
        // when
        final UserEntity userEntity = userMapper.map(user);
        // then
        assertAll(
                () -> assertThat(userEntity.getDepotCode()).isEqualTo(user.getDepot().getDepotCode()),
                () -> assertThat(userEntity.getUsername()).isEqualTo(user.getUsername())
        );
    }

    @Test
    void shouldMapFromUserEntityToUser() {
        // given
        final UserEntity userEntity = UserEntity.builder()
                .depotCode("TS1")
                .email("test@test.pl")
                .firstName("test")
                .lastName("test")
                .password("test")
                .role("admin")
                .username("test")
                .build();
        // when
        final User user = userMapper.map(userEntity);
        // then
        assertAll(
                () -> assertThat(userEntity.getDepotCode()).isEqualTo(user.getDepot().getDepotCode()),
                () -> assertThat(userEntity.getUsername()).isEqualTo(user.getUsername())
        );
    }
}
