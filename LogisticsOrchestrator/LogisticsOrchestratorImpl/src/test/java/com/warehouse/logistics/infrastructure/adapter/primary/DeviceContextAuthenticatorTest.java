package com.warehouse.logistics.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.UserIdDto;
import com.warehouse.terminal.dto.UsernameDto;
import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeviceContextAuthenticatorTest {

    private final DeviceApiService deviceApiService = mock(DeviceApiService.class);

    private final DeviceContextAuthenticator authenticator = new DeviceContextAuthenticator(deviceApiService);

    @AfterEach
    void clearContext() {
        authenticator.clear();
    }

    @Test
    void shouldExposeDeviceDepartmentInSecurityContext() {
        when(deviceApiService.authenticateByPairKey("pair-key")).thenReturn(new DeviceAuthenticationResponseDto(
                true,
                new DeviceIdDto("MB:01"),
                new DepartmentCodeDto("KT1"),
                new UserIdDto(101L),
                new UsernameDto("aezlgwa"),
                OperatorId.of(7L),
                new DepartmentId(3L)));

        authenticator.authenticateByPairKey("pair-key");

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isInstanceOf(UsernameTenantPasswordAuthenticationToken.class);
        final UsernameTenantPasswordAuthenticationToken token = (UsernameTenantPasswordAuthenticationToken) authentication;
        assertThat(token.getDepartmentId()).isEqualTo(new DepartmentId(3L));
        assertThat(token.getOperatorId()).isEqualTo(OperatorId.of(7L));
    }

    @Test
    void shouldRejectDeviceWithoutSignedInOperator() {
        when(deviceApiService.authenticateByPairKey("pair-key")).thenReturn(new DeviceAuthenticationResponseDto(
                true,
                new DeviceIdDto("MB:02"),
                new DepartmentCodeDto("KT1"),
                null,
                null,
                OperatorId.of(7L),
                new DepartmentId(3L)));

        assertThatThrownBy(() -> authenticator.authenticateByPairKey("pair-key"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No operator signed in on device");
    }
}
