package com.warehouse.auth;

import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.AuthenticationServiceImpl;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationPortImplTest {


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AuthenticationPortImpl authenticationPort;

    @Mock
    private AuthenticationService authenticationService;



    @BeforeEach
    void setup() {
        authenticationPort = new AuthenticationPortImpl(authenticationService, passwordEncoder);
    }

    @Test
    void shouldSignUpUser() {
        // given
        final RegisterRequest request = buildRegisterRequest();
        // build user response after signing
        final UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .depotCode("TST")
                .username("s-soja")
                .nonLocked(true)
                .enabled(true)
                .nonExpired(true)
                .build();

        when(authenticationService.register(any(User.class))).thenReturn(new RegisterResponse(userResponse));
        // when
        final RegisterResponse registerResponse = authenticationPort.signup(request);
        // then
        assertAll(
                () -> assertThat(registerResponse.getUserRegisterResponse()).isNotNull(),
                () -> assertThat(registerResponse.getUserRegisterResponse().getId()).isNotNull()
        );
    }

    private RegisterRequest buildRegisterRequest() {
        return new RegisterRequest(
                "test@test.pl",
                "s-soja",
                "password",
                "Test",
                "Test",
                Role.USER.name(),
                "TST"
        );
    }
}
