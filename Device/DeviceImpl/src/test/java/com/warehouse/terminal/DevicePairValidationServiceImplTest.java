package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.DeviceAccessException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.service.DevicePairValidationServiceImpl;
import com.warehouse.terminal.domain.service.UserService;

@ExtendWith(MockitoExtension.class)
class DevicePairValidationServiceImplTest {

    @Mock
    private UserService userService;

    private DevicePairValidationServiceImpl validationService;

    @BeforeEach
    void setUp() {
        this.validationService = new DevicePairValidationServiceImpl(userService);
    }

    @Test
    void shouldAllowOwnerOnPersonalDevice() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);

        assertDoesNotThrow(() -> validationService.validateOperator(device, DeviceAccessFixture.OWNER_ID));
    }

    @Test
    void shouldRejectOtherUserOnPersonalDevice() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);

        final DeviceAccessException exception = assertThrows(DeviceAccessException.class,
                () -> validationService.validateOperator(device, DeviceAccessFixture.OTHER_USER_ID));

        assertEquals("Device does not belong to user", exception.getMessage());
    }

    @Test
    void shouldAllowDepartmentUserOnSharedDevice() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        when(userService.existsByUserIdAndDepartmentCode(DeviceAccessFixture.OTHER_USER_ID,
                DeviceAccessFixture.DEPARTMENT_CODE)).thenReturn(true);

        assertDoesNotThrow(() -> validationService.validateOperator(device, DeviceAccessFixture.OTHER_USER_ID));
    }

    @Test
    void shouldRejectUserFromOtherDepartmentOnSharedDevice() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        when(userService.existsByUserIdAndDepartmentCode(DeviceAccessFixture.OTHER_USER_ID,
                DeviceAccessFixture.DEPARTMENT_CODE)).thenReturn(false);

        final DeviceAccessException exception = assertThrows(DeviceAccessException.class,
                () -> validationService.validateOperator(device, DeviceAccessFixture.OTHER_USER_ID));

        assertEquals("User does not belong to device department", exception.getMessage());
    }

    @Test
    void shouldTreatDeviceWithoutAssignmentModeAsPersonal() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, null);

        assertThrows(DeviceAccessException.class,
                () -> validationService.validateOperator(device, DeviceAccessFixture.OTHER_USER_ID));
    }

    @Test
    void shouldRejectInactiveDevice() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.BLOCKED, DeviceAssignmentMode.PERSONAL);

        final DeviceAccessException exception = assertThrows(DeviceAccessException.class,
                () -> validationService.validateOperator(device, DeviceAccessFixture.OWNER_ID));

        assertEquals("Device is not active", exception.getMessage());
    }
}
