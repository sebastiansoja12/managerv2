package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

@ExtendWith(MockitoExtension.class)
public class DevicePairRepositoryImplTest {

    @Mock
    private DevicePairReadRepository repository;

    @Mock
    private DeviceRepository<Terminal> deviceRepository;

    @InjectMocks
    private DevicePairRepositoryImpl devicePairRepository;


    @Test
    void shouldPair() {
        final TerminalId terminalId = new TerminalId(1L);
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePair devicePair = new DevicePair(terminalId, "", devicePairId);
        final Terminal terminal = new Terminal(terminalId, DeviceType.TERMINAL, new Username("user"), "KT1", "1.0.0", null, true);
        when(deviceRepository.findById(terminalId)).thenReturn(terminal);
        devicePairRepository.save(devicePair);
        verify(repository).save(any());
    }

    @Test
    void shouldFindPairIdByDeviceId() {
        final DeviceId deviceId = new DeviceId(1L);
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, null, false, null, null, null);
        when(repository.findByDevice_DeviceId(deviceId)).thenReturn(Optional.of(devicePairEntity));
        final DevicePairId actualPairId = devicePairRepository.findPairIdByDeviceId(deviceId);
        assertEquals(devicePairId, actualPairId);
    }

    @Test
    void shouldFindDevicePairByDeviceId() {
        final DeviceId deviceId = new DeviceId(1L);
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, new DeviceEntity(deviceId), false, null, null, null);
        when(repository.findByDevice_DeviceId(deviceId)).thenReturn(Optional.of(devicePairEntity));
        final DevicePair actualPair = devicePairRepository.findDevicePairByDeviceId(deviceId);
        assertEquals(devicePairEntity.getDevicePairId(), actualPair.getDeviceId().getValue());
    }

    @Test
    void shouldUpdate() {
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePair devicePair = new DevicePair(devicePairId, null, false, null, null, null);
        devicePairRepository.update(devicePair);
        verify(repository).save(any());
    }
}
