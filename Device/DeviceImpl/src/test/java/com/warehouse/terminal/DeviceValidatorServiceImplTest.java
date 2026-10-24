package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.DeviceValidationException;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;
import com.warehouse.terminal.domain.port.secondary.DeviceGenericRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.service.DeviceValidatorServiceImpl;
import com.warehouse.terminal.domain.vo.DeviceValidationRequest;
import com.warehouse.terminal.domain.vo.User;

class DeviceValidatorServiceImplTest {

    private final DeviceGenericRepository deviceRepository = mock(DeviceGenericRepository.class);
    private final DepartmentServicePort departmentServicePort = mock(DepartmentServicePort.class);
    private final UserServicePort userServicePort = mock(UserServicePort.class);
    private final DeviceValidatorServiceImpl validatorService = new DeviceValidatorServiceImpl(
            mock(DeviceVersionRepository.class), deviceRepository, departmentServicePort, userServicePort);

    @BeforeEach
    void setUp() {
        when(departmentServicePort.getDepartment(DeviceAccessFixture.DEPARTMENT_CODE))
                .thenReturn(new Department(DeviceAccessFixture.DEPARTMENT_CODE, true));
    }

    @Test
    void shouldAcceptOperatorFromDeviceDepartmentOnSharedDevice() {
        givenDevice(DeviceAssignmentMode.SHARED);
        givenUser("operator", DeviceAccessFixture.DEPARTMENT_CODE);

        assertDoesNotThrow(() -> validatorService.validateDevice(request("operator")));
    }

    @Test
    void shouldRejectOperatorFromOtherDepartmentOnSharedDevice() {
        givenDevice(DeviceAssignmentMode.SHARED);
        givenUser("operator", new DepartmentCode("KT9"));

        assertThrows(DeviceValidationException.class, () -> validatorService.validateDevice(request("operator")));
    }

    @Test
    void shouldAcceptOwnerOnPersonalDevice() {
        givenDevice(DeviceAssignmentMode.PERSONAL);
        when(userServicePort.findUserByUsername("owner")).thenReturn(new User(DeviceAccessFixture.OWNER_ID,
                DeviceAccessFixture.DEPARTMENT_CODE, new Username("owner"), DeviceAccessFixture.OPERATOR_ID));

        assertDoesNotThrow(() -> validatorService.validateDevice(request("owner")));
    }

    @Test
    void shouldRejectOtherUserOnPersonalDevice() {
        givenDevice(DeviceAssignmentMode.PERSONAL);
        givenUser("operator", DeviceAccessFixture.DEPARTMENT_CODE);

        assertThrows(DeviceValidationException.class, () -> validatorService.validateDevice(request("operator")));
    }

    private void givenDevice(final DeviceAssignmentMode assignmentMode) {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, assignmentMode);
        when(device.isActive()).thenReturn(true);
        when(deviceRepository.findById(DeviceAccessFixture.DEVICE_ID)).thenReturn(Optional.of(device));
    }

    private void givenUser(final String username, final DepartmentCode departmentCode) {
        when(userServicePort.findUserByUsername(username)).thenReturn(new User(DeviceAccessFixture.OTHER_USER_ID,
                departmentCode, new Username(username), DeviceAccessFixture.OPERATOR_ID));
    }

    private DeviceValidationRequest request(final String username) {
        return new DeviceValidationRequest(new ProcessId(UUID.randomUUID()), ServiceType.LOGISTICS_ORCHESTRATOR,
                ServiceType.TERMINAL, ProcessType.COLLECT, DeviceAccessFixture.DEVICE_ID,
                DeviceAccessFixture.DEPARTMENT_CODE, new Username(username), "1.0.0", DeviceUserType.SUPPLIER,
                DeviceType.MOBILE, true);
    }
}
