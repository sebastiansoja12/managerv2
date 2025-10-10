package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.AuthenticationPortImpl;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.provider.RefreshTokenProvider;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.domain.vo.*;
import com.warehouse.auth.infrastructure.adapter.secondary.Logger;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

@ExtendWith(MockitoExtension.class)
public class AuthenticationPortImplTest {
    
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private RefreshTokenProvider refreshTokenProvider;
    
    @Mock
    private Logger logger;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private DepartmentService departmentService;

    private JwtService jwtService;

    private AuthenticationPortImpl authenticationPort;

    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private final Long EXPIRATION = 86400000L;

	@BeforeEach
	void setup() {
		final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		final RefreshTokenGenerator refreshTokenGenerator = new RefreshTokenGeneratorImpl(refreshTokenProvider);
		final AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository,
				refreshTokenRepository, refreshTokenGenerator);
        jwtService = new JwtServiceImpl(jwtProvider);
        authenticationPort = new AuthenticationPortImpl(authenticationService, passwordEncoder, authenticationManager,
				jwtService, logger, departmentService);
	}

    @Test
    void shouldSignUpUser() {
        // given
        final RegisterRequest request = buildRegisterRequest();

        final UserResponse userResponse = UserResponse.builder()
                .departmentCode(new DepartmentCode("TST"))
                .username("s-soja")
                .nonLocked(true)
                .enabled(true)
                .nonExpired(true)
                .build();

        when(jwtProvider.getSecretKey()).thenReturn(SECRET_KEY);
        when(departmentService.existsByDepartmentCode(any(DepartmentCode.class))).thenReturn(true);
        when(userRepository.saveUser(any(User.class))).thenReturn(userResponse);
        // when
        final RegisterResponse registerResponse = authenticationPort.signup(request);
        // then
        assertAll(
                () -> assertThat(registerResponse.userResponse()).isNotNull()
        );
    }

    @Test
    void shouldLogin() {
        // given
        final LoginRequest loginRequest = new LoginRequest("s-soja", "test");

		final User userDetails = new User(new UserId(1L), "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", Role.USER,
				new DepartmentCode("TST"), "");

        doReturn(userDetails)
                .when(userRepository)
                .findByUsername(userDetails.getUsername());

        doReturn(EXPIRATION)
                .when(jwtProvider)
                .getExpiration();

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        // when
        final AuthenticationResponse response = authenticationPort.login(loginRequest);

        // then
		assertTrue(response.authenticationToken().startsWith("eyJhbGciOiJIUzM4NCJ9"));
    }

    @Test
    void shouldNotLoginWhenUserIsNotFound() {
        // given
        final String username = "fakeUser";
        final String password = "test";
        final LoginRequest loginRequest = new LoginRequest(username, password);

        doThrow(new UserNotFoundException("User was not found"))
                .when(userRepository)
                .findByUsername(username);
        // when
        final Executable executable = () -> authenticationPort.login(loginRequest);

        // then
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, executable);
        assertEquals(expectedToBe("User was not found"), exception.getMessage());
    }

    @Test
    void shouldNotSaveWhenDepartmentDoesNotExist() {
        // given
        final RegisterRequest request = new RegisterRequest(
                new DepartmentCode("TST"),
                "email",
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
        assertEquals("Department with code DepartmentCode[value=TST] does not exist", exception.getMessage());
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
        verify(refreshTokenRepository, times(1)).delete(userLogout.getRefreshToken());
    }

    private RegisterRequest buildRegisterRequest() {
        return new RegisterRequest(
                new DepartmentCode("TST"),
                "email",
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
