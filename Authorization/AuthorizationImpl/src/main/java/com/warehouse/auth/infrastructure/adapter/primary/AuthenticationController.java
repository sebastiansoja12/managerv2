package com.warehouse.auth.infrastructure.adapter.primary;

import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.infrastructure.dto.LoginRequestDto;
import com.warehouse.auth.infrastructure.dto.RegisterRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.AuthenticationResponseMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationPort authenticationPort;

    private final AuthenticationResponseMapper responseMapper;

    private final AuthCookieService authCookieService;

    private final CookieCsrfTokenRepository csrfTokenRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> create(@RequestBody final RegisterRequestDto registerRequest) {
        final RegisterRequest request = RegisterRequest.from(registerRequest);
        final RegisterResponse registerResponse = this.authenticationPort.signup(request);
        return new ResponseEntity<>(responseMapper.map(registerResponse), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequestDto loginRequest,
                                      final HttpServletResponse servletResponse) {
        try {
            final LoginRequest request = LoginRequest.from(loginRequest);
            final AuthenticationResponse authentication = authenticationPort.login(request);
            authCookieService.addAuthenticationCookies(servletResponse,
                    authentication.accessToken(), authentication.refreshToken());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
        } catch (AuthenticationErrorException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(final HttpServletRequest request,
                                        final HttpServletResponse response) {
        final String refreshToken = authCookieService.readRefreshToken(request).orElse(null);
        if (refreshToken == null) {
            authCookieService.clearAuthenticationCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
        }

        try {
            final AuthenticationResponse authentication = authenticationPort.refresh(refreshToken);
            authCookieService.addAuthenticationCookies(response,
                    authentication.accessToken(), authentication.refreshToken());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
        } catch (RuntimeException exception) {
            authCookieService.clearAuthenticationCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noStore()).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request,
                                       final HttpServletResponse response) {
        try {
            authCookieService.readRefreshToken(request).ifPresent(authenticationPort::logout);
        } finally {
            authCookieService.clearAuthenticationCookies(response);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noStore()).build();
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(final CsrfToken csrfToken,
                                                    final HttpServletRequest request,
                                                    final HttpServletResponse response) {
        csrfTokenRepository.saveToken(csrfToken, request, response);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(Map.of("token", csrfToken.getToken()));
    }
}
