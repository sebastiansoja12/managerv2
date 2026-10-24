package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
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
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.terminal.domain.exception.DeviceAccessException;
import com.warehouse.terminal.domain.model.DeviceActivationCode;
import com.warehouse.terminal.domain.model.command.DevicePairingCodeCommand;
import com.warehouse.terminal.domain.port.secondary.DeviceActivationCodeRepository;
import com.warehouse.terminal.domain.service.DeviceActivationServiceImpl;
import com.warehouse.terminal.domain.vo.ActivationCode;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeId;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeIssue;

@ExtendWith(MockitoExtension.class)
class DeviceActivationServiceImplTest {

    private static final Duration VALIDITY = Duration.ofMinutes(15);

    @Mock
    private DeviceActivationCodeRepository deviceActivationCodeRepository;

    private DeviceActivationServiceImpl deviceActivationService;

    @BeforeEach
    void setUp() {
        this.deviceActivationService = new DeviceActivationServiceImpl(deviceActivationCodeRepository, VALIDITY);
    }

    @Test
    void shouldStoreOnlyHashOfIssuedCodeAndDiscardPreviousCodes() {
        final ArgumentCaptor<DeviceActivationCode> captor = ArgumentCaptor.forClass(DeviceActivationCode.class);

        final DeviceActivationCodeIssue issue = deviceActivationService.issue(DeviceAccessFixture.DEVICE_ID);

        final InOrder order = inOrder(deviceActivationCodeRepository);
        order.verify(deviceActivationCodeRepository).deleteUnusedByDeviceId(DeviceAccessFixture.DEVICE_ID);
        order.verify(deviceActivationCodeRepository).save(captor.capture());
        assertEquals(issue.activationCode().hash(), captor.getValue().getCodeHash());
        assertEquals(issue.expiresAt(), captor.getValue().getExpiresAt());
        assertTrue(issue.expiresAt().isAfter(Instant.now().plus(VALIDITY).minusSeconds(5)));
    }

    @Test
    void shouldIssueUserPairingCodeAndDiscardPreviousUserCodes() {
        final ArgumentCaptor<DeviceActivationCode> captor = ArgumentCaptor.forClass(DeviceActivationCode.class);

        deviceActivationService.issueForUser(new DevicePairingCodeCommand(DeviceAccessFixture.OWNER_ID,
                DeviceAccessFixture.OPERATOR_ID, DeviceAccessFixture.DEPARTMENT_CODE));

        verify(deviceActivationCodeRepository).deleteUnusedByUserId(DeviceAccessFixture.OWNER_ID);
        verify(deviceActivationCodeRepository).save(captor.capture());
        assertTrue(captor.getValue().isUserPairing());
        assertEquals(DeviceAccessFixture.DEPARTMENT_CODE, captor.getValue().getDepartmentCode());
    }

    @Test
    void shouldReturnDeviceIdWhenCodeIsUsable() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final DeviceActivationCode code = activationCode(activationCode, Instant.now().plus(VALIDITY), null);
        when(deviceActivationCodeRepository.findByCodeHash(activationCode.hash())).thenReturn(Optional.of(code));
        when(deviceActivationCodeRepository.markUsed(any(DeviceActivationCodeId.class), any(Instant.class)))
                .thenReturn(true);

        final DeviceActivationCode consumed = deviceActivationService.consume(activationCode);

        assertEquals(DeviceAccessFixture.DEVICE_ID, consumed.getDeviceId());
    }

    @Test
    void shouldRejectExpiredCode() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final DeviceActivationCode code = activationCode(activationCode, Instant.now().minusSeconds(1), null);
        when(deviceActivationCodeRepository.findByCodeHash(activationCode.hash())).thenReturn(Optional.of(code));

        assertThrows(DeviceAccessException.class, () -> deviceActivationService.consume(activationCode));
        verify(deviceActivationCodeRepository, never()).markUsed(any(), any());
    }

    @Test
    void shouldRejectCodeUsedConcurrently() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        final DeviceActivationCode code = activationCode(activationCode, Instant.now().plus(VALIDITY), null);
        when(deviceActivationCodeRepository.findByCodeHash(activationCode.hash())).thenReturn(Optional.of(code));
        when(deviceActivationCodeRepository.markUsed(any(DeviceActivationCodeId.class), any(Instant.class)))
                .thenReturn(false);

        assertThrows(DeviceAccessException.class, () -> deviceActivationService.consume(activationCode));
    }

    @Test
    void shouldRejectUnknownCode() {
        final ActivationCode activationCode = new ActivationCode("ABCD-EFGH");
        when(deviceActivationCodeRepository.findByCodeHash(activationCode.hash())).thenReturn(Optional.empty());

        assertThrows(DeviceAccessException.class, () -> deviceActivationService.consume(activationCode));
    }

    private DeviceActivationCode activationCode(final ActivationCode activationCode,
                                                final Instant expiresAt,
                                                final Instant usedAt) {
        return new DeviceActivationCode(DeviceActivationCodeId.generate(), DeviceAccessFixture.DEVICE_ID, null, null,
                null, activationCode.hash(), Instant.now().minus(Duration.ofMinutes(1)), expiresAt, usedAt);
    }
}
