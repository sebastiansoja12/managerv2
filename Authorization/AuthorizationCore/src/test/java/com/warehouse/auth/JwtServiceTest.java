package com.warehouse.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        final UserDetails userDetails = new User("test", "test", Collections.emptyList());
        // when
        final String jwtToken = jwtService.generateToken(extraClaims, userDetails);
        // then
        assertTrue(StringUtils.isNotEmpty(jwtToken));
    }

    @Test
    void shouldCheckIfTokenIsValid() {

    }

    @Test
    void shouldTokenBeInvalid() {

    }
}
