package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.RefreshTokenGeneratorImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;

public class RefreshTokenGeneratorImplTest {

    private final RefreshTokenProvider refreshTokenProvider = new RefreshTokenProvider();
    private RefreshTokenGeneratorImpl rerouteTokenGenerator;

    @BeforeEach
    void setup() {
        rerouteTokenGenerator = new RefreshTokenGeneratorImpl(refreshTokenProvider);
    }

    @Test
    void shouldGenerateRandomToken() {
        // given
        final User user = User.builder()
                .username("s-soja")
                .firstName("Sebastian")
                .lastName("Soja")
                .depotCode("TST")
                .email("sebastian5152@wp.pl")
                .password("test")
                .role(Role.USER)
                .build();
        // when
        final String token = rerouteTokenGenerator.generateToken(user);
        // then
        assertThat(token).isNotEmpty();
    }
}
