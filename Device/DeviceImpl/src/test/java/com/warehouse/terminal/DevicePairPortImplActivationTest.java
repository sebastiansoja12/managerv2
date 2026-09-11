package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.exception.DeviceAccessException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.command.DeviceActivationCommand;
import com.warehouse.terminal.domain.model.command.DeviceHeartbeatCommand;
import com.warehouse.terminal.domain.model.command.DeviceOperatorSessionCommand;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.service.DeviceActivationService;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.DevicePairService;
import com.warehouse.terminal.domain.service.DevicePairValidationService;
import com.warehouse.terminal.domain.service.DeviceVersionService;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.vo.ActivationCode;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeId;
import com.warehouse.terminal.domain.vo.DeviceActivationResult;
import com.warehouse.terminal.domain.vo.DeviceHardware;
import com.warehouse.terminal.domain.vo.DeviceOperatorSessionResult;
import com.warehouse.terminal.domain.vo.DevicePairKey;
import com.warehouse.terminal.domain.vo.DeviceSoftware;
import com.warehouse.terminal.domain.vo.IssuedDevicePairKey;

@ExtendWith(MockitoExtension.class)
class DevicePairPortImplActivationTest {

    @Mock
    private DeviceGenericService deviceGenericService;

    @Mock
    private UserService userService;

    @Mock
    private DevicePairService devicePairService;

    @Mock
    private DeviceVersionService deviceVersionService;

    @Mock
    private DevicePairValidationService devicePairValidationService;

    @Mock
    private DeviceActivationService deviceActivationService;

    private DevicePairPortImpl devicePairPort;

    @BeforeEach
    void setUp() {
        this.devicePairPort = new DevicePairPortImpl(deviceGenericService, userService, devicePairService,
                deviceVersionService, devicePairValidationService, deviceActivationService, new OperatorContext());
    }

    @Test
    void shouldReturnPairKeyWhenDeviceIsActivated() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        final IssuedDevicePairKey issuedPairKey = new IssuedDevicePairKey(
                new DevicePairKey(DeviceAccessFixture.PAIR_KEY), Instant.now().plus(Duration.ofDays(180)));
        when(deviceActivationService.consume(activationCode)).thenReturn(deviceActivationCode());
        when(deviceGenericService.findByDeviceIdForAuthentication(DeviceAccessFixture.DEVICE_ID))
                .thenReturn(Optional.of(device));
        when(devicePairService.activateDevice(device)).thenReturn(issuedPairKey);

        final DeviceActivationResult result = devicePairPort.activate(activationCommand(activationCode, null));

        assertEquals(DeviceAccessFixture.PAIR_KEY, result.pairKey().value());
        assertEquals(device, result.device());
    }

    @Test
    void shouldRejectActivationOfInactiveDevice() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final Device device = DeviceAccessFixture.device(DeviceStatus.LOST, DeviceAssignmentMode.PERSONAL);
        when(deviceActivationService.consume(activationCode)).thenReturn(deviceActivationCode());
        when(deviceGenericService.findByDeviceIdForAuthentication(DeviceAccessFixture.DEVICE_ID))
                .thenReturn(Optional.of(device));

        assertThrows(DeviceAccessException.class, () -> devicePairPort.activate(activationCommand(activationCode, null)));
        verify(devicePairService, never()).activateDevice(any());
    }

    @Test
    void shouldCreatePersonalMobileDeviceWhenUserPairsUnknownPhone() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final DeviceId newDeviceId = new DeviceId("MB:00000000-0000-0000-0000-000000000099");
        when(deviceActivationService.consume(activationCode)).thenReturn(userPairingCode());
        when(deviceGenericService.findByExternalSystemId("app:installation-1")).thenReturn(Optional.empty());
        when(deviceGenericService.nextDeviceId(DeviceType.MOBILE)).thenReturn(newDeviceId);
        when(devicePairService.activateDevice(any())).thenReturn(new IssuedDevicePairKey(
                new DevicePairKey(DeviceAccessFixture.PAIR_KEY), Instant.now().plus(Duration.ofDays(180))));
        final ArgumentCaptor<Device> createdDevice = ArgumentCaptor.forClass(Device.class);

        final DeviceActivationResult result = devicePairPort.activate(activationCommand(activationCode, "installation-1"));

        verify(deviceGenericService).create(createdDevice.capture());
        assertInstanceOf(Mobile.class, createdDevice.getValue());
        assertEquals(newDeviceId, result.device().getDeviceId());
        assertEquals(DeviceAccessFixture.OWNER_ID, createdDevice.getValue().getUserId());
        assertEquals(DeviceAccessFixture.DEPARTMENT_CODE, createdDevice.getValue().getOwnership().getDepartmentCode());
        assertEquals("app:installation-1", createdDevice.getValue().getIdentity().getExternalSystemId());
        assertEquals("Pixel 8", createdDevice.getValue().getHardware().getModel());
    }

    @Test
    void shouldPairKnownPhoneAndMovePersonalDeviceToScanningUser() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);
        when(device.getUserId()).thenReturn(DeviceAccessFixture.OTHER_USER_ID);
        when(deviceActivationService.consume(activationCode)).thenReturn(userPairingCode());
        when(deviceGenericService.findByExternalSystemId("app:installation-1")).thenReturn(Optional.of(device));
        when(devicePairService.activateDevice(device)).thenReturn(new IssuedDevicePairKey(
                new DevicePairKey(DeviceAccessFixture.PAIR_KEY), Instant.now().plus(Duration.ofDays(180))));

        devicePairPort.activate(activationCommand(activationCode, "installation-1"));

        verify(deviceGenericService).assignUser(DeviceAccessFixture.DEVICE_ID, DeviceAccessFixture.OWNER_ID);
        verify(deviceGenericService, never()).create(any());
    }

    @Test
    void shouldKeepOwnerOfSharedDeviceWhenPairingFromAccount() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        when(deviceActivationService.consume(activationCode)).thenReturn(userPairingCode());
        when(deviceGenericService.findByExternalSystemId("app:installation-1")).thenReturn(Optional.of(device));
        when(devicePairService.activateDevice(device)).thenReturn(new IssuedDevicePairKey(
                new DevicePairKey(DeviceAccessFixture.PAIR_KEY), Instant.now().plus(Duration.ofDays(180))));

        devicePairPort.activate(activationCommand(activationCode, "installation-1"));

        verify(deviceGenericService, never()).assignUser(any(), any());
    }

    @Test
    void shouldRequireInstallationIdForAccountPairing() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        when(deviceActivationService.consume(activationCode)).thenReturn(userPairingCode());

        final DeviceAccessException exception = assertThrows(DeviceAccessException.class,
                () -> devicePairPort.activate(activationCommand(activationCode, " ")));

        assertEquals("Installation identifier is required to pair a new device", exception.getMessage());
        verify(devicePairService, never()).activateDevice(any());
    }

    @Test
    void shouldRejectActivationCodeForInactiveDevice() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.RETIRED, DeviceAssignmentMode.PERSONAL);
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);

        assertThrows(DeviceAccessException.class,
                () -> devicePairPort.issueActivationCode(DeviceAccessFixture.DEVICE_ID));
        verify(deviceActivationService, never()).issue(any());
    }

    @Test
    void shouldStartOperatorSessionWhenOperatorIsAllowed() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        final DevicePair devicePair = DeviceAccessFixture.devicePair();
        final Instant expiresAt = Instant.now().plus(Duration.ofHours(12));
        final DevicePair session = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OTHER_USER_ID,
                expiresAt);
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY)).thenReturn(Optional.of(devicePair));
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);
        when(devicePairService.startOperatorSession(devicePair, DeviceAccessFixture.OTHER_USER_ID))
                .thenReturn(session);

        final DeviceOperatorSessionResult result = devicePairPort.startOperatorSession(
                new DeviceOperatorSessionCommand(new DevicePairKey(DeviceAccessFixture.PAIR_KEY),
                        DeviceAccessFixture.OTHER_USER_ID));

        verify(devicePairValidationService).validateOperator(device, DeviceAccessFixture.OTHER_USER_ID);
        assertEquals(expiresAt, result.sessionExpiresAt());
    }

    @Test
    void shouldRejectOperatorSessionWithInvalidPairKey() {
        when(devicePairService.findValidByPairKey("unknown")).thenReturn(Optional.empty());

        final DeviceAccessException exception = assertThrows(DeviceAccessException.class,
                () -> devicePairPort.startOperatorSession(new DeviceOperatorSessionCommand(
                        new DevicePairKey("unknown"), DeviceAccessFixture.OWNER_ID)));

        assertEquals("Invalid device credentials", exception.getMessage());
    }

    @Test
    void shouldUpdateVersionAndMarkDeviceSeenOnHeartbeat() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);
        final DevicePair devicePair = DeviceAccessFixture.devicePair();
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY)).thenReturn(Optional.of(devicePair));
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);

        final boolean updateRequired = devicePairPort.heartbeat(new DeviceHeartbeatCommand(
                new DevicePairKey(DeviceAccessFixture.PAIR_KEY), "1.1.0", null, null, null, null)).updateRequired();

        verify(deviceGenericService).updateVersionField(DeviceAccessFixture.DEVICE_ID, "1.1.0");
        verify(devicePairService).markSeen(devicePair);
        verify(deviceGenericService, never()).updateHardware(any(), any());
        verify(deviceGenericService, never()).updateSoftware(any(), any());
        assertFalse(updateRequired);
    }

    @Test
    void shouldReportHardwareAndOperatingSystemOnHeartbeat() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);
        when(device.getDeviceType()).thenReturn(DeviceType.MOBILE);
        final DevicePair devicePair = DeviceAccessFixture.devicePair();
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY)).thenReturn(Optional.of(devicePair));
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);
        final ArgumentCaptor<DeviceHardware> hardwareCaptor = ArgumentCaptor.forClass(DeviceHardware.class);
        final ArgumentCaptor<DeviceSoftware> softwareCaptor = ArgumentCaptor.forClass(DeviceSoftware.class);

        devicePairPort.heartbeat(new DeviceHeartbeatCommand(new DevicePairKey(DeviceAccessFixture.PAIR_KEY),
                "1.0.0", "Zebra", "TC52", "Android", "13"));

        verify(deviceGenericService).updateHardware(eq(DeviceAccessFixture.DEVICE_ID), hardwareCaptor.capture());
        verify(deviceGenericService).updateSoftware(eq(DeviceAccessFixture.DEVICE_ID), softwareCaptor.capture());
        assertEquals("TC52", hardwareCaptor.getValue().getModel());
        assertEquals("Android", softwareCaptor.getValue().getOsName());
        assertEquals("1.0.0", softwareCaptor.getValue().getAppVersion());
    }

    @Test
    void shouldNotWriteUnchangedHardwareAndSoftwareOnHeartbeat() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);
        final DeviceHardware hardware = new DeviceHardware();
        hardware.setManufacturer("Zebra");
        hardware.setModel("TC52");
        final DeviceSoftware software = new DeviceSoftware();
        software.setOsName("Android");
        software.setOsVersion("13");
        software.setAppVersion("1.0.0");
        when(device.getDeviceType()).thenReturn(DeviceType.MOBILE);
        when(device.getHardware()).thenReturn(hardware);
        when(device.getSoftware()).thenReturn(software);
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY))
                .thenReturn(Optional.of(DeviceAccessFixture.devicePair()));
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);

        devicePairPort.heartbeat(new DeviceHeartbeatCommand(new DevicePairKey(DeviceAccessFixture.PAIR_KEY),
                "1.0.0", "Zebra", "TC52", "Android", "13"));

        verify(deviceGenericService, never()).updateHardware(any(), any());
        verify(deviceGenericService, never()).updateSoftware(any(), any());
        verify(deviceGenericService, never()).updateVersionField(any(), any());
    }

    @Test
    void shouldNotReportOperatingSystemOfScanner() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.SHARED);
        when(device.getDeviceType()).thenReturn(DeviceType.SCANNER);
        final DevicePair devicePair = DeviceAccessFixture.devicePair();
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY)).thenReturn(Optional.of(devicePair));
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);

        devicePairPort.heartbeat(new DeviceHeartbeatCommand(new DevicePairKey(DeviceAccessFixture.PAIR_KEY),
                "1.0.0", null, null, "Web", "Chrome 130"));

        verify(deviceGenericService, never()).updateSoftware(any(), any());
    }

    @Test
    void shouldNotUpdateVersionWhenHeartbeatReportsCurrentVersion() {
        final Device device = DeviceAccessFixture.device(DeviceStatus.ACTIVE, DeviceAssignmentMode.PERSONAL);
        final DevicePair devicePair = DeviceAccessFixture.devicePair();
        when(devicePairService.findValidByPairKey(DeviceAccessFixture.PAIR_KEY)).thenReturn(Optional.of(devicePair));
        when(deviceGenericService.findByDeviceId(DeviceAccessFixture.DEVICE_ID)).thenReturn(device);

        devicePairPort.heartbeat(new DeviceHeartbeatCommand(new DevicePairKey(DeviceAccessFixture.PAIR_KEY), "1.0.0",
                null, null, null, null));

        verify(deviceGenericService, never()).updateVersionField(any(), any());
    }

    private DeviceActivationCommand activationCommand(final ActivationCode activationCode, final String installationId) {
        return new DeviceActivationCommand(activationCode, installationId, null, "1.0.0", "Google", "Pixel 8",
                "Android", "15");
    }

    private DeviceActivationCode deviceActivationCode() {
        return new DeviceActivationCode(DeviceActivationCodeId.generate(), DeviceAccessFixture.DEVICE_ID, null, null,
                null, "hash", Instant.now(), Instant.now().plus(Duration.ofMinutes(15)), null);
    }

    private DeviceActivationCode userPairingCode() {
        return DeviceActivationCode.issueForUser(DeviceAccessFixture.OWNER_ID, DeviceAccessFixture.OPERATOR_ID,
                DeviceAccessFixture.DEPARTMENT_CODE, "hash", Instant.now(), Duration.ofMinutes(15));
    }
}
