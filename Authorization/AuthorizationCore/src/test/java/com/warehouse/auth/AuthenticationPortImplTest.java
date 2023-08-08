package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;

@ExtendWith(MockitoExtension.class)
public class AuthenticationPortImplTest {


    @Mock
    private AuthenticationService authenticationService;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private JwtService jwtService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AuthenticationPortImpl authenticationPort;

	@BeforeEach
	void setup() {
		authenticationPort = new AuthenticationPortImpl(authenticationService, passwordEncoder, authenticationManager,
				jwtService);
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

    @Test
    void shouldThrowAuthenticationErrorException() {
        // given
        final RegisterRequest request = null;
        // when
        final Executable executable = () -> authenticationPort.signup(request);
        // then
        final AuthenticationErrorException exception = assertThrows(AuthenticationErrorException.class, executable);
        assertEquals("Request is not correct", exception.getMessage());
    }

    @Test
    void shouldNotSaveWhenUsernameFieldMissing() {
        // given
        final RegisterRequest request = new RegisterRequest(
                "test@test.pl",
                null,
                "password",
                "Test",
                "Test",
                Role.USER.name(),
                "TST"
        );
        // when
        final Executable executable = () -> authenticationPort.signup(request);
        // then
        final AuthenticationErrorException exception = assertThrows(AuthenticationErrorException.class, executable);
        assertEquals("Username cannot be empty", exception.getMessage());
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
