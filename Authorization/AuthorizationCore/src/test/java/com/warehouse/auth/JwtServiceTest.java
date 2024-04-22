package com.warehouse.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import com.warehouse.commonassets.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;


@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    private JwtService jwtService;

    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private final Long EXPIRATION = 86400000L;

    @BeforeEach
    void setup() {
        jwtService = new JwtServiceImpl(jwtProvider);
    }

    @Test
    void shouldGenerateToken() {
        // given
        final User user = User.builder()
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("Test")
                .lastName("Test")
                .role(Role.ADMIN)
                .username("test")
                .build();

        doReturn(EXPIRATION)
                .when(jwtProvider)
                .getExpiration();

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        // when
        final String jwtToken = jwtService.generateToken(user);
        // then
        assertTrue(StringUtils.isNotEmpty(jwtToken));
        assertTrue(jwtToken.startsWith("eyJhbGciOiJIUzM4NCJ9"));
    }

    @Test
    void shouldCheckIfTokenIsValid() {
        // given
        final User user = User.builder()
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("Test")
                .lastName("Test")
                .role(Role.ADMIN)
                .username("test")
                .build();

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        // create token
        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg4NDg2Mzc2LCJleHAiOjkyNDkzMzU2NDAwfQ" +
                ".CqWbN5YR41WobyySEKZGbWFrUFpIFSCb4TwQt5DzlLM";

        // when
        final boolean isValid = jwtService.isTokenValid(jwtToken, user);

        // then
        assertTrue(isValid);
    }

    @Test
    void shouldTokenBeInvalid() {
        // given
        final User user = User.builder()
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("Test")
                .lastName("Test")
                .role(Role.ADMIN)
                .username("fake")
                .build();

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        // create nonvalid jwttoken
        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg4NDgzMjk2LCJleHAiOjE2ODg0ODQ3MzZ9" +
                ".8Ms_0WQZRMl-uyOfASWGvujzVhmgesOjIuqEXd0vAjo";

        // when
        final Executable executable = () -> jwtService.isTokenValid(jwtToken, user);
        // then
        final ExpiredJwtException exception = assertThrows(ExpiredJwtException.class, executable);
        assertTrue(exception.getMessage().startsWith("JWT expired at"));
    }

    @Test
    void shouldExtractUsernameFromJwt() {
        // given
        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();
        // create token
        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg4NDg2Mzc2LCJleHAiOjkyNDkzMzU2NDAwfQ" +
                ".CqWbN5YR41WobyySEKZGbWFrUFpIFSCb4TwQt5DzlLM";
        // when
        final String username = jwtService.extractUsername(jwtToken);
        // then
        assertEquals("test", username);
    }
}
