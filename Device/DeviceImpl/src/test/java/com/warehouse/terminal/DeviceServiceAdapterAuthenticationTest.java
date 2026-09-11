package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.DevicePairService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.infrastructure.adapter.primary.DeviceServiceAdapter;
import com.warehouse.terminal.dto.DeviceAccessDto;
import com.warehouse.terminal.response.DeviceAuthenticationResponseDto;

@ExtendWith(MockitoExtension.class)
class DeviceServiceAdapterAuthenticationTest {

    @Mock
    private DevicePairService devicePairService;

    @Mock
    private DeviceGenericService deviceGenericService;

    @Mock
    private UserService userService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private OperatorContext operatorContext;

    private DeviceServiceAdapter deviceServiceAdapter;

    @BeforeEach
    void setUp() {
        this.deviceServiceAdapter = new DeviceServiceAdapter(devicePairService, deviceGenericService, userService,
                departmentRepository, operatorContext);
    }

    @Test
    void shouldAuthenticateSharedDeviceWithoutOperatorWhenNobodySignedIn() {
        givenDevice(DeviceAssignmentMode.SHARED, DeviceAccessFixture.devicePair());

        final DeviceAuthenticationResponseDto authentication =
                deviceServiceAdapter.authenticateByPairKey(DeviceAccessFixture.PAIR_KEY);

        assertTrue(authentication.value());
        assertNull(authentication.userId());
    }

    @Test
    void shouldAuthenticateSharedDeviceAsSignedInOperator() {
        givenDevice(DeviceAssignmentMode.SHARED, DeviceAccessFixture.devicePairWithOperator(
                DeviceAccessFixture.OTHER_USER_ID, Instant.now().plus(Duration.ofHours(1))));

        final DeviceAuthenticationResponseDto authentication =
                deviceServiceAdapter.authenticateByPairKey(DeviceAccessFixture.PAIR_KEY);

        assertEquals(DeviceAccessFixture.OTHER_USER_ID.value(), authentication.userId().value());
    }

    @Test
    void shouldIgnoreExpiredOperatorSessionOnSharedDevice() {
        givenDevice(DeviceAssignmentMode.SHARED, DeviceAccessFixture.devicePairWithOperator(
                DeviceAccessFixture.OTHER_USER_ID, Instant.now().minus(Duration.ofMinutes(1))));

        final DeviceAuthenticationResponseDto authentication =
                deviceServiceAdapter.authenticateByPairKey(DeviceAccessFixture.PAIR_KEY);

        assertNull(authentication.userId());
    }

    @Test
    void shouldAuthenticatePersonalDeviceAsOwnerWithoutSession() {
        givenDevice(DeviceAssignmentMode.PERSONAL, DeviceAccessFixture.devicePair());

        final DeviceAuthenticationResponseDto authentication =
                deviceServiceAdapter.authenticateByPairKey(DeviceAccessFixture.PAIR_KEY);

        assertEquals(DeviceAccessFixture.OWNER_ID.value(), authentication.userId().value());
    }

    @Test
    void shouldExposeAssignmentModeAndActiveOperatorInDeviceAccess() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        final Instant sessionExpiresAt = Instant.now().plus(Duration.ofHours(1));
        final DevicePair devicePair = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OTHER_USER_ID,
                sessionExpiresAt);
        devicePair.markSeen(Instant.parse("2026-10-23T08:00:00Z"));
        when(deviceGenericService.findAll()).thenReturn(List.of(device));
        when(devicePairService.findExistingByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(Optional.of(devicePair));

        final DeviceAccessDto access = deviceServiceAdapter.findAllAccess().getFirst();

        assertEquals("SHARED", access.assignmentMode());
        assertEquals(DeviceAccessFixture.OTHER_USER_ID.value(), access.operatorUserId());
        assertEquals(sessionExpiresAt, access.operatorSessionExpiresAt());
        assertEquals(Instant.parse("2026-10-23T08:00:00Z"), access.lastSeenAt());
    }

    @Test
    void shouldHideExpiredOperatorSessionInDeviceAccess() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        final DevicePair devicePair = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OTHER_USER_ID,
                Instant.now().minus(Duration.ofMinutes(1)));
        when(deviceGenericService.findAll()).thenReturn(List.of(device));
        when(devicePairService.findExistingByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(Optional.of(devicePair));

        final DeviceAccessDto access = deviceServiceAdapter.findAllAccess().getFirst();

        assertNull(access.operatorUserId());
        assertNull(access.operatorSessionExpiresAt());
    }

    private void givenDevice(final DeviceAssignmentMode assignmentMode, final DevicePair devicePair) {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, assignmentMode);
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY)).thenReturn(Optional.of(devicePair));
        when(deviceGenericService.findByDeviceIdForAuthentication(DeviceAccessFixture.DEVICE_ID))
                .thenReturn(Optional.of(device));
    }
}
