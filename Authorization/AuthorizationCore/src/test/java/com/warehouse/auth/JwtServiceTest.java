package com.warehouse.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;

import io.micrometer.common.util.StringUtils;

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

    }

    @Test
    void shouldTokenBeInvalid() {

    }

    @Test
    void shouldExtractUsernameFromJwt() {

    }


}
