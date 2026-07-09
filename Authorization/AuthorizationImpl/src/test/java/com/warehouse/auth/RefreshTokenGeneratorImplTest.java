package com.warehouse.auth;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.RefreshTokenGeneratorImpl;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentCode("TST"), "");
        // when
        final String token = refreshTokenGenerator.generateToken(user);
        // then
        assertThat(token).isNotEmpty();
    }
}
