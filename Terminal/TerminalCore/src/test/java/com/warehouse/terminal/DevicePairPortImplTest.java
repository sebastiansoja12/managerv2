package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.service.*;

@ExtendWith(MockitoExtension.class)
public class DevicePairPortImplTest {

    @Mock
    private DeviceVersionRepository deviceVersionRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DevicePairRepository devicePairRepository;
    
    @Mock
    private SupplierRepository supplierRepository;

    private DevicePairPortImpl devicePairPort;
    
    @BeforeEach
    void setup() {
		final TerminalValidatorService terminalValidatorService = new TerminalValidatorServiceImpl(
				deviceVersionRepository, departmentRepository, userRepository, supplierRepository, deviceRepository);
        final TerminalService terminalService = new TerminalServiceImpl(deviceRepository);
        final UserService userService = new UserServiceImpl(userRepository);
        final DevicePairService devicePairService = new DevicePairServiceImpl(devicePairRepository);
        final DeviceVersionService deviceVersionService = new DeviceVersionServiceImpl(deviceVersionRepository);
		devicePairPort = new DevicePairPortImpl(terminalValidatorService, terminalService, userService,
				devicePairService, deviceVersionService);
    }

    @Test
    void shouldBeConnected() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        final DevicePair devicePair = new DevicePair(true);
        doReturn(devicePair)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(deviceId);
        // when
        final boolean connected = devicePairPort.isConnected(deviceId);
        // then
        assertTrue(connected);
    }

    @Test
    void shouldNotBeConnected() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        final DevicePair devicePair = new DevicePair(false);
        doReturn(devicePair)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(deviceId);
        // when
        final boolean connected = devicePairPort.isConnected(deviceId);
        // then
        assertFalse(connected);
    }

    @Test
    void shouldBeValid() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final Device device = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        final DevicePair devicePair = new DevicePair(true);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(mock(DeviceVersion.class))
                .when(deviceVersionRepository)
                .find(terminalId);
        doReturn(devicePair)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(terminalId);
        // when
        final boolean valid = devicePairPort.isValid(terminalId);
        // then
        assertTrue(valid);
    }

    @Test
    void shouldNotBeValidWhenDeviceIsEmpty() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final DevicePair devicePair = new DevicePair(false);
        doReturn(null)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(mock(DeviceVersion.class))
                .when(deviceVersionRepository)
                .find(terminalId);
        doReturn(devicePair)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(terminalId);
        // when
        final boolean valid = devicePairPort.isValid(terminalId);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldNotBeValidWhenDeviceVersionIsEmpty() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final Device device = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        final DevicePair devicePair = new DevicePair(false);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(null)
                .when(deviceVersionRepository)
                .find(terminalId);
        doReturn(devicePair)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(terminalId);
        // when
        final boolean valid = devicePairPort.isValid(terminalId);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldNotBeValidWhenDevicePairIsEmpty() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final Device device = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(mock(DeviceVersion.class))
                .when(deviceVersionRepository)
                .find(terminalId);
        doReturn(null)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(terminalId);
        // when
        final boolean valid = devicePairPort.isValid(terminalId);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldUserBeValid() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final UserId userId = new UserId(1L);
        final Device device = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(true)
                .when(userRepository)
                .existsById(userId);
        // when
        final boolean valid = devicePairPort.isUserValid(terminalId, userId);
        // then
        assertTrue(valid);
    }

    @Test
    void shouldUserNotBeValidWhenDeviceDoesNotExist() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final UserId userId = new UserId(1L);
        doReturn(null)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(true)
                .when(userRepository)
                .existsById(userId);
        // when
        final boolean valid = devicePairPort.isUserValid(terminalId, userId);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldUserNotBeValidWhenDeviceUserIdIsNotEqualToGivenUserId() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final UserId userId = new UserId(1L);
        final Device device = createDevice(terminalId, "1.0", new UserId(2L), "KT1", true);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        doReturn(true)
                .when(userRepository)
                .existsById(userId);
        // when
        final boolean valid = devicePairPort.isUserValid(terminalId, userId);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldVersionBeValid() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final DeviceVersion deviceVersion = new DeviceVersion("1.0", terminalId);
        final Device device = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        // when
        final boolean valid = devicePairPort.isVersionValid(terminalId, deviceVersion);
        // then
        assertTrue(valid);
    }

    @Test
    void shouldVersionNotBeValid() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final DeviceVersion deviceVersion = new DeviceVersion("2.0", terminalId);
        final Device device = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        doReturn(device)
                .when(deviceRepository)
                .findById(terminalId);
        // when
        final boolean valid = devicePairPort.isVersionValid(terminalId, deviceVersion);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldUpdateBeRequired() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final DeviceVersion deviceVersion = new DeviceVersion("1.0", terminalId);
        doReturn(new DeviceVersion("2.0", terminalId))
                .when(deviceVersionRepository)
                .find(terminalId);
        // when
        final boolean valid = devicePairPort.updateRequired(terminalId, deviceVersion);
        // then
        assertTrue(valid);
    }

    @Test
    void shouldUpdateNotBeRequired() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final DeviceVersion deviceVersion = new DeviceVersion("2.0", terminalId);
        doReturn(new DeviceVersion("2.0", terminalId))
                .when(deviceVersionRepository)
                .find(terminalId);
        // when
        final boolean valid = devicePairPort.updateRequired(terminalId, deviceVersion);
        // then
        assertFalse(valid);
    }

    @Test
    void shouldUnpairDevice() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final Terminal terminal = createDevice(terminalId, "1.0", new UserId(1L), "KT1", true);
        final DevicePair devicePair = new DevicePair(true);
        doReturn(devicePair)
                .when(devicePairRepository)
                .findDevicePairByDeviceId(terminalId);
        // when
        devicePairPort.unpair(terminal);
        // then
        assertFalse(devicePair.isPaired());

    }

    // TODO testy z parowaniem terminala

    private static Terminal createDevice(final TerminalId terminalId, final String version, final UserId userId,
                                         final String departmentCode, final Boolean active) {
        return new Terminal(terminalId, DeviceType.TERMINAL, userId, departmentCode, version, Instant.now(), active);
    }
}
