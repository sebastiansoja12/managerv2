package com.warehouse.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.auth.configuration.AuthTestConfiguration;
import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.LoginRequest;
import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = AuthTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/user_repository.xml")
public class AuthenticationIntegrationTest {

    @Autowired
    private AuthenticationPort authenticationPort;

    @Autowired
    private AuthenticationReadRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenReadRepository refreshTokenReadRepository;

    private final static String USERNAME = "s-soja";

    private final static String PASSWORD = "password";

    @Test
    void shouldLoginUser() {
        // given
        final LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);
        // when
        final AuthenticationResponse response = authenticationPort.login(loginRequest);
        // then
        assertTrue(response.getAuthenticationToken().startsWith("eyJhbGciOiJIUzM4NCJ9"));
        assertThatJwtUsernameTokenIsCorrect(response.getAuthenticationToken());
    }

    @Test
    void shouldNotLoginUserWhenBadCredentialsAreProvided() {
        // given
        final LoginRequest loginRequest = new LoginRequest("fakeUsername", PASSWORD);
        // when
        final Executable executable = () -> authenticationPort.login(loginRequest);
        // then
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, executable);
        assertEquals(expectedToBe("User was not found"), exception.getMessage());
    }

    @Test
    void shouldFindUserByUsername() {
        // given
        final String username = "s-soja";
        // when
        final User user = authenticationPort.findUser(username);
        // then
        assertAll(
                () -> assertEquals(expectedToBe("s-soja"), user.getUsername()),
                () -> assertEquals(expectedToBe(Role.ADMIN), user.getRole()),
                () -> assertEquals(expectedToBe(Role.ADMIN.getAuthorities()), user.getAuthorities()),
                () -> assertEquals(expectedToBe("Sebastian"), user.getFirstName())
        );
    }

    @Test
    void shouldNotFindUser() {
        // given
        final String username = "fakeUser";
        // when
        final Executable executable = () -> authenticationPort.findUser(username);
        // then
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, executable);
        assertEquals(expectedToBe("User was not found"), exception.getMessage());
    }

    @Test
    @DatabaseSetup("/dataset/refresh_token.xml")
    void shouldLogoutUser() {
        // given
        final String refreshToken = "12345";
        final String username = "s-soja";
        final RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken, username);
        // when
        authenticationPort.logout(refreshTokenRequest);
        // then
        assertFalse(refreshTokenReadRepository.findByToken(refreshToken).isPresent());
    }

    @Test
    @DatabaseSetup("/dataset/refresh_token.xml")
    void shouldNotLogoutUser() {
        // given
        final String refreshToken = "0";
        final String username = "s-soja";
        final RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken, username);
        // when
        authenticationPort.logout(refreshTokenRequest);
        // then
        assertFalse(refreshTokenReadRepository.findByToken(refreshToken).isPresent());
    }

    private void assertThatJwtUsernameTokenIsCorrect(String authenticationToken) {
        final String username = jwtService.extractUsername(authenticationToken);
        assertEquals(expectedToBe(USERNAME), username);
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role(Role.ADMIN)
                .email("sebastian5152@wp.pl")
                .depotCode("TST")
                .firstName("Sebastian")
                .lastName("Soja")
                .build();
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
