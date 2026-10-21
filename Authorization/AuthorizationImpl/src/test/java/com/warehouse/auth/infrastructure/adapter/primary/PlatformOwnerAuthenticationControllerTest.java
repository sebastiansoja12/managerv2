package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.configuration.AuthCookieService;
import com.warehouse.auth.configuration.AuthProperties;
import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.auth.domain.vo.ImpersonationSession;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.PlatformOwnerAuthentication;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class PlatformOwnerAuthenticationControllerTest {

    private static final PlatformOwnerId PLATFORM_OWNER_ID = new PlatformOwnerId(3L);

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort = mock(PlatformOwnerAuthenticationPort.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        final JwtProvider jwtProvider = new JwtProvider();
        jwtProvider.setExpiration(60_000L);
        jwtProvider.setRefreshTokenExpiration(120_000L);
        final PlatformOwnerAuthenticationController controller = new PlatformOwnerAuthenticationController(
                platformOwnerAuthenticationPort, new AuthCookieService(new AuthProperties(), jwtProvider));
        mockMvc = standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAccessAndRefreshCookiesAfterLogin() throws Exception {
        when(platformOwnerAuthenticationPort.login(new LoginRequest("owner", "secret")))
                .thenReturn(new PlatformOwnerAuthentication("access-token", "refresh-token"));

        final MvcResult result = mockMvc.perform(post("/platform/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"owner\",\"password\":\"secret\"}"))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getHeaders("Set-Cookie"))
                .anyMatch(cookie -> cookie.startsWith("PLATFORM-AUTH-TOKEN=access-token"))
                .anyMatch(cookie -> cookie.startsWith("PLATFORM-REFRESH-TOKEN=refresh-token")
                        && cookie.contains("Path=/v1/api/platform/auth"));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
        when(platformOwnerAuthenticationPort.login(new LoginRequest("owner", "bad")))
                .thenThrow(new AuthenticationErrorException("Invalid username or password"));

        mockMvc.perform(post("/platform/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"owner\",\"password\":\"bad\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRotateCookiesOnRefresh() throws Exception {
        when(platformOwnerAuthenticationPort.refresh("old-refresh"))
                .thenReturn(new PlatformOwnerAuthentication("new-access", "new-refresh"));

        final MvcResult result = mockMvc.perform(post("/platform/auth/refresh")
                        .cookie(new Cookie("PLATFORM-REFRESH-TOKEN", "old-refresh")))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getHeaders("Set-Cookie"))
                .anyMatch(cookie -> cookie.startsWith("PLATFORM-REFRESH-TOKEN=new-refresh"));
    }

    @Test
    void shouldReturnUnauthorizedOnRefreshWithoutCookie() throws Exception {
        mockMvc.perform(post("/platform/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRevokeRefreshTokenOnLogout() throws Exception {
        mockMvc.perform(post("/platform/auth/logout")
                        .cookie(new Cookie("PLATFORM-REFRESH-TOKEN", "refresh-token")))
                .andExpect(status().isNoContent());

        verify(platformOwnerAuthenticationPort).logout("refresh-token");
    }

    @Test
    void shouldStartImpersonationAndReplaceOrganizationCookies() throws Exception {
        authenticatePlatformOwner();
        when(platformOwnerAuthenticationPort.startImpersonation(PLATFORM_OWNER_ID, OperatorId.of(20L)))
                .thenReturn(new ImpersonationSession("impersonation-token", new UserId(5L), "superadmin", OperatorId.of(20L)));

        final MvcResult result = mockMvc.perform(post("/platform/auth/impersonations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"operatorId\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.username").value("superadmin"))
                .andReturn();

        assertThat(result.getResponse().getHeaders("Set-Cookie"))
                .anyMatch(cookie -> cookie.startsWith("AUTH-TOKEN=impersonation-token"))
                .anyMatch(cookie -> cookie.startsWith("REFRESH-TOKEN=") && cookie.contains("Max-Age=0"));
    }

    @Test
    void shouldReturnConflictWhenOperatorHasNoSuperadmin() throws Exception {
        authenticatePlatformOwner();
        when(platformOwnerAuthenticationPort.startImpersonation(PLATFORM_OWNER_ID, OperatorId.of(21L)))
                .thenThrow(new IllegalStateException("Operator 21 has no active superadmin"));

        mockMvc.perform(post("/platform/auth/impersonations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"operatorId\":21}"))
                .andExpect(status().isConflict());
    }

    private void authenticatePlatformOwner() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(PLATFORM_OWNER_ID, null, List.of()));
    }
}
