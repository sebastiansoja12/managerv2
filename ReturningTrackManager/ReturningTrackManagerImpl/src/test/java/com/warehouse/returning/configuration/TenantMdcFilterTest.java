package com.warehouse.returning.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.DecodedApiOperator;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.RestException;

import jakarta.servlet.http.Cookie;

class TenantMdcFilterTest {

    @Test
    void shouldAuthenticateRequestWithManagerAccessTokenCookie() throws Exception {
        final ApiKeyService apiKeyService = mock(ApiKeyService.class);
        final TenantMdcFilter filter = new TenantMdcFilter(apiKeyService, "AUTH-TOKEN");
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v2/api/returns/123");
        request.setCookies(new Cookie("AUTH-TOKEN", "access-token"));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean filterChainInvoked = new AtomicBoolean(false);
        when(apiKeyService.decodeJwt("access-token")).thenReturn(new DecodedApiOperator(
                new UserId(11L), new DepartmentCode("KT1"), 10001L, "operator"));

        filter.doFilter(request, response, (servletRequest, servletResponse) -> filterChainInvoked.set(true));

        assertTrue(filterChainInvoked.get());
        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldReturnUnauthorizedWhenAccessTokenCookieIsExpired() throws Exception {
        final ApiKeyService apiKeyService = mock(ApiKeyService.class);
        final TenantMdcFilter filter = new TenantMdcFilter(apiKeyService, "AUTH-TOKEN");
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v2/api/returns/123");
        request.setCookies(new Cookie("AUTH-TOKEN", "expired-token"));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final AtomicBoolean filterChainInvoked = new AtomicBoolean(false);
        when(apiKeyService.decodeJwt("expired-token")).thenThrow(new RestException(401, "Invalid or expired JWT token"));

        filter.doFilter(request, response, (servletRequest, servletResponse) -> filterChainInvoked.set(true));

        assertEquals(401, response.getStatus());
        assertFalse(filterChainInvoked.get());
    }
}
