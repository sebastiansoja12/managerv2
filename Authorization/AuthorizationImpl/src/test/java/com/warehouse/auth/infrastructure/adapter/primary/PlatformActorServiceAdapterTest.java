package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.auth.infrastructure.dto.PlatformActorDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlatformActorServiceAdapterTest {

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort = mock(PlatformOwnerAuthenticationPort.class);

    private final PlatformActorServiceAdapter adapter = new PlatformActorServiceAdapter(platformOwnerAuthenticationPort);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldReturnPlatformOwnerWorkingInPanel() {
        final PlatformOwnerId platformOwnerId = new PlatformOwnerId(3L);
        when(platformOwnerAuthenticationPort.getPlatformOwner(platformOwnerId)).thenReturn(
                PlatformOwner.create(platformOwnerId, "owner", "encoded", "Platform", "Owner", "owner@test.pl"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(platformOwnerId, null, List.of()));

        assertThat(adapter.currentActor()).contains(new PlatformActorDto(3L, "owner", false, null, null));
    }

    @Test
    void shouldReturnPlatformOwnerImpersonatingOperatorUser() {
        SecurityContextHolder.getContext().setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                new UserId(5L), OperatorId.of(20L), null, null, List.of()));
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE,
                new Impersonation(new PlatformOwnerId(3L), "owner"));
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThat(adapter.currentActor()).contains(new PlatformActorDto(3L, "owner", true, 5L, 20L));
    }

    @Test
    void shouldReturnEmptyForRegularUser() {
        SecurityContextHolder.getContext().setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                new UserId(5L), OperatorId.of(20L), null, null, List.of()));
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        assertThat(adapter.currentActor()).isEmpty();
    }

    @Test
    void shouldReturnEmptyWithoutAuthentication() {
        assertThat(adapter.currentActor()).isEmpty();
    }
}
