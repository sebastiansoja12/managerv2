package com.warehouse.auth;

import com.warehouse.auth.configuration.AuthTestConfiguration;
import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = AuthTestConfiguration.class)
public class AuthenticationRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Test
    void shouldLoginAndSaveRefreshToken() {
        // given
        final AuthenticationResponse response = AuthenticationResponse.builder()
                .authenticationToken("test")
                .refreshToken("refreshToken")
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(600L))
                .role("[admin]")
                .username("s-soja")
                .build();
        // when
        final AuthenticationResponse responseAfterLogin = userRepository.login(response);
        // then
        assertAll(
                () -> assertThat(responseAfterLogin.getRefreshToken()).isEqualTo(response.getRefreshToken()),
                () -> assertThat(responseAfterLogin.getAuthenticationToken()).isEqualTo(response.getAuthenticationToken())
        );
    }

    @Test
    void shouldSignup() {
        // given
        final UserEntity userEntity = UserEntity.builder()
                .username("s-soja")
                .password("password")
                .firstName("Sebastian")
                .lastName("Soja")
                .email("sebastian5152@wp.plk")
                .depotCode("KT1")
                .role("[admin]")
                .build();
        // when
        userRepository.signup(userEntity);
        // then
        assertThat(userEntity.getId()).isGreaterThan(0);
    }

    @Test
    void shouldNotSignupTwoSameUsers() {
        // given
        final UserEntity userEntity = UserEntity.builder()
                .username("s-soja")
                .password("password")
                .firstName("Sebastian")
                .lastName("Soja")
                .email("sebastian5152@wp.plk")
                .depotCode("KT1")
                .role("[admin]")
                .build();

        final UserEntity userEntity2 = UserEntity.builder()
                .username("s-soja")
                .password("password")
                .firstName("Sebastian")
                .lastName("Soja")
                .email("sebastian5152@wp.plk")
                .depotCode("KT1")
                .role("[admin]")
                .build();
        // when
        // and: first user is saved
        userRepository.signup(userEntity);
        // for second we expect exception
        final Executable executable = () -> userRepository.signup(userEntity2);
        // then: DataIntegrityViolationException is thrown
        final DataIntegrityViolationException dataIntegrity =
                assertThrows(DataIntegrityViolationException.class, executable);
        assertThat(dataIntegrity.getLocalizedMessage()).contains("could not execute statement; SQL [n/a];");
    }

    @Test
    void shouldNotSignupWhenUsernameIsNull() {
        // given
        final UserEntity userEntity = UserEntity.builder()
                .username(null)
                .password("password")
                .firstName("Sebastian")
                .lastName("Soja")
                .email("sebastian5152@wp.plk")
                .depotCode("KT1")
                .role("[admin]")
                .build();
        // when
        final Executable executable = () -> userRepository.signup(userEntity);
        // then
        final DataIntegrityViolationException dataIntegrity =
                assertThrows(DataIntegrityViolationException.class, executable);
        assertThat(dataIntegrity.getMessage()).contains("not-null property references a null or transient");
    }

    @Test
    void shouldLogout() {
        // given
        final RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken("token");
        refreshToken.setCreatedDate(Instant.now());
        // refresh token is saved in db
        refreshTokenRepository.save(refreshToken);
        // when
        userRepository.logout(refreshToken.getToken());
        // then
        assertFalse(refreshTokenRepository.existsById(refreshToken.getId()));
    }
}
