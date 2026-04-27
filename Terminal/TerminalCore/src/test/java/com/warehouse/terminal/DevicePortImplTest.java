package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.UserNotFoundException;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.*;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.primary.DevicePortImpl;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.*;

@ExtendWith(MockitoExtension.class)
class DevicePortImplTest {

    @Mock
    private DeviceGenericService deviceGenericService;

    @Mock
    private UserService userService;

    @Mock
    private DepartmentServicePort departmentServicePort;

    @Mock
    private UserServicePort userServicePort;

    @Captor
    private ArgumentCaptor<Device> deviceCaptor;

    @Captor
    private ArgumentCaptor<DeviceSettings> deviceSettingsCaptor;

    private DevicePortImpl devicePort;

    @BeforeEach
    void setUp() {
        this.devicePort = new DevicePortImpl(deviceGenericService, userService, departmentServicePort, userServicePort);
    }

    @Test
    void shouldCreateTerminalAndReturnDeviceId() {
        final DeviceCreateCommand command = createCommand(DeviceType.TERMINAL, null, null);
        final DeviceId nextId = new DeviceId("TL:11111111-1111-1111-1111-111111111111");

        when(userServicePort.findUserById(command.getUserId())).thenReturn(user(command.getUserId(), command.getDepartmentCode()));
        when(departmentServicePort.getDepartment(any())).thenReturn(department(command.getDepartmentCode()));
        when(deviceGenericService.nextDeviceId(DeviceType.TERMINAL)).thenReturn(nextId);

        final DeviceCreateResult result = devicePort.create(command);

        assertEquals(nextId, result.deviceId());
        verify(deviceGenericService).create(deviceCaptor.capture());
        final Device created = deviceCaptor.getValue();
        assertInstanceOf(Terminal.class, created);
        assertEquals(DeviceType.TERMINAL, created.getDeviceType());
        assertEquals(nextId, created.getDeviceId());
        assertEquals(command.getUserId(), created.getUserId());
        assertEquals(command.getDepartmentCode().value(), created.getDepartmentCode().value());
        assertEquals(50.0614d, created.getLocation().getLatitude());
        assertEquals(19.9366d, created.getLocation().getLongitude());
    }

    @Test
    void shouldCreateScannerAndKeepScanConfiguration() {
        final DeviceCreateCommand command = createCommand(DeviceType.SCANNER, Scanner.ScanType.BARCODE, Scanner.ScannerType.HANDHELD);
        final DeviceId nextId = new DeviceId("SC:11111111-1111-1111-1111-111111111111");

        when(userServicePort.findUserById(command.getUserId())).thenReturn(user(command.getUserId(), command.getDepartmentCode()));
        when(departmentServicePort.getDepartment(any())).thenReturn(department(command.getDepartmentCode()));
        when(deviceGenericService.nextDeviceId(DeviceType.SCANNER)).thenReturn(nextId);

        final DeviceCreateResult result = devicePort.create(command);

        assertEquals(nextId, result.deviceId());
        verify(deviceGenericService).create(deviceCaptor.capture());
        final Scanner created = assertInstanceOf(Scanner.class, deviceCaptor.getValue());
        assertEquals(Scanner.ScanType.BARCODE, created.getScanType());
        assertEquals(Scanner.ScannerType.HANDHELD, created.getScannerType());
    }

    @Test
    void shouldThrowWhenCreateUserDoesNotExist() {
        final DeviceCreateCommand command = createCommand(DeviceType.MOBILE, null, null);

        when(userServicePort.findUserById(command.getUserId())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> devicePort.create(command));
        verify(departmentServicePort, never()).getDepartment(any());
        verify(deviceGenericService, never()).nextDeviceId(any());
        verify(deviceGenericService, never()).create(any());
    }

    @Test
    void shouldAssignUserByUsername() {
        final DeviceUserRequest request = new DeviceUserRequest(
                new DeviceId("TL:assign-user"),
                new Username("john.doe")
        );
        final User user = user(new UserId(9001L), new DepartmentCode("KT1"));
        when(userService.findByUsername(request.username())).thenReturn(user);

        devicePort.changeUserTo(request);

        verify(deviceGenericService).assignUser(request.deviceId(), user.userId());
    }

    @Test
    void shouldThrowWhenAssignUserUsernameDoesNotExist() {
        final DeviceUserRequest request = new DeviceUserRequest(
                new DeviceId("TL:assign-user-missing"),
                new Username("missing.user")
        );
        when(userService.findByUsername(request.username())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> devicePort.changeUserTo(request));
        verify(deviceGenericService, never()).assignUser(any(), any());
    }

    @Test
    void shouldDelegateVersionChange() {
        final DeviceVersionRequest request = new DeviceVersionRequest(new DeviceId("MB:change-version"), "2.4.1");

        devicePort.changeVersionTo(request);

        verify(deviceGenericService).updateVersion(request.deviceId(), request.version());
    }

    @Test
    void shouldValidateReferencesBeforeUpdateDevice() {
        final UserId userId = new UserId(101L);
        final DepartmentCode departmentCode = new DepartmentCode("KT1");
        final OwnershipProfile ownership = OwnershipProfile.initializeOwnership("OPERATOR", new UserId(202L), new DepartmentCode("KT2"), null);
        final DeviceUpdateCommand request = new DeviceUpdateCommand(
                new DeviceId("TL:update-1"),
                null,
                null,
                null,
                userId,
                departmentCode,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ownership
        );

        when(userService.findByUserId(userId)).thenReturn(user(userId, departmentCode));
        when(userService.findByUserId(ownership.getUserId())).thenReturn(user(ownership.getUserId(), ownership.getDepartmentCode()));
        when(departmentServicePort.getDepartment(any())).thenReturn(department(new DepartmentCode("KTX")));

        devicePort.updateDevice(request);

        verify(userService).findByUserId(userId);
        verify(userService).findByUserId(ownership.getUserId());
        verify(departmentServicePort).getDepartment(argThat(code -> "KT1".equals(code.value())));
        verify(departmentServicePort).getDepartment(argThat(code -> "KT2".equals(code.value())));
        verify(deviceGenericService).updateDevice(request);
    }

    @Test
    void shouldThrowWhenUpdateDeviceUserDoesNotExist() {
        final UserId userId = new UserId(333L);
        final DeviceUpdateCommand request = new DeviceUpdateCommand(
                new DeviceId("TL:update-user-missing"),
                null,
                null,
                null,
                userId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        when(userService.findByUserId(userId)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> devicePort.updateDevice(request));
        verify(deviceGenericService, never()).updateDevice(any());
    }

    @Test
    void shouldThrowWhenUpdateOwnershipUserDoesNotExist() {
        final OwnershipProfile ownership = OwnershipProfile.initializeOwnership("OPERATOR", new UserId(777L), new DepartmentCode("KT1"), null);
        final DeviceOwnershipUpdateCommand request = new DeviceOwnershipUpdateCommand(new DeviceId("TL:ownership-missing"), ownership);
        when(userService.findByUserId(ownership.getUserId())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> devicePort.updateOwnershipField(request));
        verify(deviceGenericService, never()).updateOwnership(any(), any());
    }

    @Test
    void shouldValidateOwnershipAndUpdateOwnershipField() {
        final OwnershipProfile ownership = OwnershipProfile.initializeOwnership("OPERATOR", new UserId(888L), new DepartmentCode("KT3"), null);
        final DeviceOwnershipUpdateCommand request = new DeviceOwnershipUpdateCommand(new DeviceId("TL:ownership-ok"), ownership);
        when(userService.findByUserId(ownership.getUserId())).thenReturn(user(ownership.getUserId(), ownership.getDepartmentCode()));
        when(departmentServicePort.getDepartment(any())).thenReturn(department(ownership.getDepartmentCode()));

        devicePort.updateOwnershipField(request);

        verify(userService).findByUserId(ownership.getUserId());
        verify(departmentServicePort).getDepartment(argThat(code -> "KT3".equals(code.value())));
        verify(deviceGenericService).updateOwnership(request.deviceId(), ownership);
    }

    @Test
    void shouldConvertAndDelegateSettingsUpdate() {
        final DeviceSettingsRequest request = new DeviceSettingsRequest(
                new DeviceId("TL:settings-1"),
                true,
                false,
                true
        );

        devicePort.updateSettings(request);

        verify(deviceGenericService).updateSettings(eq(request.getDeviceId()), deviceSettingsCaptor.capture());
        final DeviceSettings settings = deviceSettingsCaptor.getValue();
        assertEquals(request.getDeviceId(), settings.getDeviceId());
        assertEquals(Boolean.TRUE, settings.getCrossCourierDelivery());
        assertEquals(Boolean.FALSE, settings.getValidateResponsibleUser());
        assertEquals(Boolean.TRUE, settings.getValidateDepartmentCode());
    }

    @Test
    void shouldReturnAllDevicesFromGenericService() {
        final List<Device> devices = List.of(
                new Terminal(
                        new DeviceId("TL:all-1"),
                        DeviceStatus.ACTIVE,
                        null,
                        null,
                        null,
                        null,
                        null,
                        OwnershipProfile.initializeOwnership("OPERATOR", new UserId(1L), new DepartmentCode("KT1"), null)
                ),
                new Mobile(
                        new DeviceId("MB:all-2"),
                        DeviceStatus.ACTIVE,
                        null,
                        null,
                        null,
                        null,
                        null,
                        OwnershipProfile.initializeOwnership("OPERATOR", new UserId(2L), new DepartmentCode("KT2"), null)
                )
        );
        when(deviceGenericService.findAll()).thenReturn(devices);

        final List<Device> result = devicePort.allDevices();

        assertSame(devices, result);
    }

    @Test
    void shouldReturnDeviceByIdFromGenericService() {
        final DeviceId deviceId = new DeviceId("SC:get-1");
        final Device device = new Scanner(
                deviceId,
                new DeviceIdentity(),
                new DeviceHardware(),
                new DeviceNetwork(),
                OwnershipProfile.initializeOwnership("OPERATOR", new UserId(3L), new DepartmentCode("KT3"), null),
                Scanner.ScanType.QRCODE,
                Scanner.ScannerType.RING
        );
        when(deviceGenericService.findByDeviceId(deviceId)).thenReturn(device);

        final Device result = devicePort.getDevice(deviceId);

        assertSame(device, result);
    }

    @Test
    void shouldDelegateTypeActiveAndStatusUpdates() {
        final DeviceId deviceId = new DeviceId("TL:field-1");
        final DeviceTypeUpdateCommand typeCommand = new DeviceTypeUpdateCommand(deviceId, DeviceType.MOBILE);
        final DeviceActiveUpdateCommand activeCommand = new DeviceActiveUpdateCommand(deviceId, false);
        final DeviceStatusUpdateCommand statusCommand = new DeviceStatusUpdateCommand(deviceId, DeviceStatus.LOST);

        devicePort.updateDeviceType(typeCommand);
        devicePort.updateActiveField(activeCommand);
        devicePort.updateStatusField(statusCommand);

        verify(deviceGenericService).updateDeviceType(deviceId, DeviceType.MOBILE);
        verify(deviceGenericService).updateActive(deviceId, false);
        verify(deviceGenericService).updateStatus(deviceId, DeviceStatus.LOST);
    }

    @Test
    void shouldDelegateIdentityHardwareSoftwareUpdates() {
        final DeviceId deviceId = new DeviceId("TL:field-2");
        final DeviceIdentity identity = new DeviceIdentity();
        final DeviceHardware hardware = new DeviceHardware();
        final com.warehouse.terminal.domain.vo.DeviceSoftware software = new com.warehouse.terminal.domain.vo.DeviceSoftware();
        final DeviceIdentityUpdateCommand identityCommand = new DeviceIdentityUpdateCommand(deviceId, identity);
        final DeviceHardwareUpdateCommand hardwareCommand = new DeviceHardwareUpdateCommand(deviceId, hardware);
        final DeviceSoftwareUpdateCommand softwareCommand = new DeviceSoftwareUpdateCommand(deviceId, software);

        devicePort.updateIdentityField(identityCommand);
        devicePort.updateHardwareField(hardwareCommand);
        devicePort.updateSoftwareField(softwareCommand);

        verify(deviceGenericService).updateIdentity(eq(deviceId), same(identity));
        verify(deviceGenericService).updateHardware(eq(deviceId), same(hardware));
        verify(deviceGenericService).updateSoftware(eq(deviceId), same(software));
    }

    @Test
    void shouldDelegateNetworkSecurityLocationAndVersionUpdates() {
        final DeviceId deviceId = new DeviceId("TL:field-3");
        final DeviceNetwork network = new DeviceNetwork();
        final SecurityProfile security = new SecurityProfile();
        final DeviceLocation location = new DeviceLocation();
        final DeviceNetworkUpdateCommand networkCommand = new DeviceNetworkUpdateCommand(deviceId, network);
        final DeviceSecurityUpdateCommand securityCommand = new DeviceSecurityUpdateCommand(deviceId, security);
        final DeviceLocationUpdateCommand locationCommand = new DeviceLocationUpdateCommand(deviceId, location);
        final DeviceVersionUpdateCommand versionCommand = new DeviceVersionUpdateCommand(deviceId, "9.9.9");

        devicePort.updateNetworkField(networkCommand);
        devicePort.updateSecurityField(securityCommand);
        devicePort.updateLocationField(locationCommand);
        devicePort.updateVersionField(versionCommand);

        verify(deviceGenericService).updateNetwork(eq(deviceId), same(network));
        verify(deviceGenericService).updateSecurity(eq(deviceId), same(security));
        verify(deviceGenericService).updateLocation(eq(deviceId), same(location));
        verify(deviceGenericService).updateVersionField(deviceId, "9.9.9");
    }

    private DeviceCreateCommand createCommand(final DeviceType deviceType,
                                              final Scanner.ScanType scanType,
                                              final Scanner.ScannerType scannerType) {
        return new DeviceCreateCommand(
                new UserId(1001L),
                new SupplierCode("SUP-01"),
                "1.0.0",
                DeviceUserType.OPERATOR,
                new DepartmentCode("KT1"),
                deviceType,
                scanType,
                scannerType,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private User user(final UserId userId, final DepartmentCode departmentCode) {
        return new User(userId, departmentCode, new Username("john.doe"));
    }

    private Department department(final DepartmentCode departmentCode) {
        return new Department(departmentCode, 50.0614d, 19.9366d);
    }
}
