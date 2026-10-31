package com.warehouse.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.PlatformOwnerAuthenticationFilter;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

import jakarta.servlet.FilterChain;

class TenantMdcFilterTest {

    private final TenantMdcFilter filter = new TenantMdcFilter(new ApiExposureProperties());

    private final Map<String, String> capturedMdc = new HashMap<>();

    private final FilterChain capturingChain = (request, response) -> capturedMdc.putAll(MDC.getCopyOfContextMap());

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldFillMdcFromAuthenticationToken() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                new UserId(7L), new OperatorId(3L), new DepartmentId(5L), "jwt", List.of()));
        final MockHttpServletRequest request = request();
        request.setAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE, "jan.kowalski");

        filter.doFilter(request, new MockHttpServletResponse(), capturingChain);

        assertThat(capturedMdc).containsEntry("operator", "3")
                .containsEntry("user", "7")
                .containsEntry("username", "jan.kowalski")
                .containsEntry("method", "GET");
        assertThat(MDC.getCopyOfContextMap()).isNullOrEmpty();
    }

    @Test
    void shouldFillMdcForPlatformOwner() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new PlatformOwnerId(2L), "token", List.of()));
        final MockHttpServletRequest request = request();
        request.setAttribute(PlatformOwnerAuthenticationFilter.PLATFORM_OWNER_USERNAME_ATTRIBUTE, "owner");

        filter.doFilter(request, new MockHttpServletResponse(), capturingChain);

        assertThat(capturedMdc).containsEntry("operator", "PLATFORM")
                .containsEntry("user", "platform-owner-2")
                .containsEntry("username", "owner");
    }

    @Test
    void shouldRejectRequestWithoutAuthentication() throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request(), response, capturingChain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(capturedMdc).isEmpty();
    }

    private static MockHttpServletRequest request() {
        return new MockHttpServletRequest("GET", "/v1/api/shipments/1");
    }
}
