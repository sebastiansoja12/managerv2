package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.RefreshTokenGeneratorImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;
import com.warehouse.commonassets.identificator.DepartmentCode;

public class RefreshTokenGeneratorImplTest {

    private final RefreshTokenProvider refreshTokenProvider = new RefreshTokenProvider();
    private RefreshTokenGeneratorImpl refreshTokenGenerator;

    @BeforeEach
    void setup() {
        refreshTokenGenerator = new RefreshTokenGeneratorImpl(refreshTokenProvider);
    }

    @Test
    void shouldGenerateRandomToken() {
        // given
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", Role.USER,
                new DepartmentCode("TST"), "");
        // when
        final String token = refreshTokenGenerator.generateToken(user);
        // then
        assertThat(token).isNotEmpty();
    }
}
