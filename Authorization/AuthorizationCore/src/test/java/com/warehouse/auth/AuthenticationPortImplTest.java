package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.warehouse.auth.domain.vo.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.Logger;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AuthenticationPortImplTest {


    @Mock
    private AuthenticationService authenticationService;
    
    @Mock
    private AuthenticationManager authenticationManager;


    @Mock
    private Logger logger;

    @Mock
    private JwtProvider jwtProvider;

    private JwtService jwtService;

    private AuthenticationPortImpl authenticationPort;

    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private final Long EXPIRATION = 86400000L;

	@BeforeEach
	void setup() {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        jwtService = new JwtServiceImpl(jwtProvider);
		authenticationPort = new AuthenticationPortImpl(authenticationService, passwordEncoder, authenticationManager,
                jwtService, logger);
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
    void shouldLogin() {
        // given
        final String authenticationToken = "authenticationToken";
        final LoginRequest loginRequest = new LoginRequest("s-soja", "test");
        final LoginResponse loginResponse = new LoginResponse(new Token(authenticationToken));
        final Authentication authentication = new UsernamePasswordAuthenticationToken("s-soja", "test");
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        final User userDetails = User.builder()
                .username("s-soja")
                .firstName("Sebastian")
                .lastName("Soja")
                .depotCode("TST")
                .email("sebastian5152@wp.pl")
                .password("test")
                .role(Role.USER)
                .build();

        /*doReturn(authentication)
                .when(authenticationManager)
                .authenticate(usernamePasswordAuthenticationToken);*/

        doReturn(userDetails)
                .when(authenticationService)
                .findUser("s-soja");

        doReturn(EXPIRATION)
                .when(jwtProvider)
                .getExpiration();

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        doReturn(loginResponse)
                .when(authenticationService)
                .login(userDetails);

        // when
        final AuthenticationResponse response = authenticationPort.login(loginRequest);

        // then
		assertTrue(response.getAuthenticationToken().startsWith("eyJhbGciOiJIUzM4NCJ9"));

        // check if generated refresh token is correct
		assertEquals(expectedToBe(loginResponse.getRefreshToken().getValue().length()),
				response.getRefreshToken().length());
    }

    @Test
    void shouldNotLoginWhenUserIsNotFound() {
        // given
        final String username = "fakeUser";
        final String password = "test";
        final LoginRequest loginRequest = new LoginRequest(username, password);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

       /* doReturn(authentication)
                .when(authenticationManager)
                .authenticate(usernamePasswordAuthenticationToken);*/

        doThrow(new UserNotFoundException("User was not found"))
                .when(authenticationService)
                .findUser(username);
        // when
        final Executable executable = () -> authenticationPort.login(loginRequest);

        // then
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, executable);
        assertEquals(expectedToBe("User was not found"), exception.getMessage());
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

    @Test
    void shouldLogout() {
        // given
        final String refreshToken = "refreshToken";
        final String username = "s-soja";
        final RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken, username);
        final UserLogout userLogout = UserLogout.builder()
                .refreshToken(refreshToken)
                .username(username)
                .build();
        // when
        authenticationPort.logout(refreshTokenRequest);
        // then
        verify(authenticationService, times(1)).logout(userLogout);
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

    private <T> T expectedToBe(T value) {
        return value;
    }
}
