package com.warehouse.auth;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.JwtServiceImpl;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


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
        final User user = new User(new UserId(1L), "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentCode("TST"), "");

        doReturn(EXPIRATION)
                .when(jwtProvider)
                .getExpiration();

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        final String jwtToken = jwtService.generateToken(user);
        assertTrue(StringUtils.isNotEmpty(jwtToken));
        assertTrue(jwtToken.startsWith("eyJhbGciOiJIUzM4NCJ9"));
    }

    @Test
    void shouldCheckIfTokenIsValid() {
        final User user = new User(null, "test", "test", "Test", "Test", "test@test.pl", User.Role.ADMIN,
                new DepartmentCode("TST"), "");
        user.setDeleted(false);

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg4NDg2Mzc2LCJleHAiOjkyNDkzMzU2NDAwfQ" +
                ".CqWbN5YR41WobyySEKZGbWFrUFpIFSCb4TwQt5DzlLM";

        final boolean isValid = jwtService.isTokenValid(jwtToken, user);

        assertTrue(isValid);
    }

    @Test
    void shouldTokenBeInvalid() {
        final User user = new User(null, "test", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentCode("TST"), "");

        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();

        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg4NDgzMjk2LCJleHAiOjE2ODg0ODQ3MzZ9" +
                ".8Ms_0WQZRMl-uyOfASWGvujzVhmgesOjIuqEXd0vAjo";

        final Executable executable = () -> jwtService.isTokenValid(jwtToken, user);
        final ExpiredJwtException exception = assertThrows(ExpiredJwtException.class, executable);
        assertTrue(exception.getMessage().startsWith("JWT expired at"));
    }

    @Test
    void shouldExtractUsernameFromJwt() {
        doReturn(SECRET_KEY)
                .when(jwtProvider)
                .getSecretKey();
        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg4NDg2Mzc2LCJleHAiOjkyNDkzMzU2NDAwfQ" +
                ".CqWbN5YR41WobyySEKZGbWFrUFpIFSCb4TwQt5DzlLM";
        final String username = jwtService.extractUsername(jwtToken);
        assertEquals("test", username);
    }
}
