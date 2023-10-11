package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.AuthenticationServiceImpl;
import com.warehouse.auth.domain.service.RefreshTokenGenerator;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenGenerator refreshTokenGenerator;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        authenticationService = new AuthenticationServiceImpl(userRepository, refreshTokenRepository,
                refreshTokenGenerator);
    }

    @Test
    void shouldRegister() {
        // given
        final User user = User.builder()
                .username("s-soja")
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("test")
                .lastName("test")
                .password("password")
                .role(Role.ADMIN)
                .build();

        doReturn(mock(UserResponse.class))
                .when(userRepository)
                .saveUser(user);
        // when
        final RegisterResponse registerResponse = authenticationService.register(user);
        // then
        assertThat(registerResponse.getUserRegisterResponse().getId()).isNotNull();
    }

    @Test
    void shouldLogin() {
        // given
        final User user = User.builder()
                .username("s-soja")
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("test")
                .lastName("test")
                .password("password")
                .role(Role.ADMIN)
                .build();

        final RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .build();

        final Token token = new Token("90b20782-8882-4f46-a14e-e0fa761fb7c8");

        doReturn(token)
                .when(refreshTokenRepository)
                .save(refreshToken);
        // when
        final LoginResponse response = authenticationService.login(user);
        // then
        assertEquals(expectedToBe("90b20782-8882-4f46-a14e-e0fa761fb7c8"), response.getRefreshToken().getValue());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
