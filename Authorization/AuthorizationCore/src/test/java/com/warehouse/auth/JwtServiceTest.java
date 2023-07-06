package com.warehouse.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.function.Executable;

public class JwtServiceTest {

    private final JwtProvider jwtProvider = new JwtProvider();
    private JwtService jwtService;


    @BeforeEach
    void setup() {
        jwtService = new JwtServiceImpl(jwtProvider);
    }

    @Test
    void shouldGenerateToken() {
        // given
        final Map<String, Object> extraClaims = new HashMap<>();
        final UserEntity user = UserEntity.builder()
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("Test")
                .lastName("Test")
                .role(Role.ADMIN)
                .id(1L)
                .username("test")
                .build();
        // when
        final String jwtToken = jwtService.generateToken(extraClaims, user);
        // then
        assertTrue(StringUtils.isNotEmpty(jwtToken));
        assertTrue(jwtToken.startsWith("eyJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    void shouldCheckIfTokenIsValid() {
        // given
        final UserEntity user = UserEntity.builder()
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("Test")
                .lastName("Test")
                .role(Role.ADMIN)
                .id(1L)
                .username("test")
                .build();

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
        final UserEntity user = UserEntity.builder()
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("Test")
                .lastName("Test")
                .role(Role.ADMIN)
                .id(1L)
                .username("fake")
                .build();

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
