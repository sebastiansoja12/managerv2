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

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

@ExtendWith(MockitoExtension.class)
public class DevicePairRepositoryImplTest {

    @Mock
    private DevicePairReadRepository repository;

    @InjectMocks
    private DevicePairRepositoryImpl devicePairRepository;

    @Test
    void shouldPair() {
        // given
        final TerminalId terminalId = new TerminalId(1L);
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePair devicePair = new DevicePair(terminalId, "", devicePairId);
        // when
        devicePairRepository.save(devicePair);
        // then
        verify(repository).save(any());
    }

    @Test
    void shouldFindPairIdByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, null, false, null, null, null);
        when(repository.findByDevice_DeviceId(deviceId)).thenReturn(Optional.of(devicePairEntity));
        // when
        final DevicePairId actualPairId = devicePairRepository.findPairIdByDeviceId(deviceId);
        // then
        assertEquals(devicePairId, actualPairId);
    }

    @Test
    void shouldFindDevicePairByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, null, false, null, null, null);
        when(repository.findByDevice_DeviceId(deviceId)).thenReturn(Optional.of(devicePairEntity));
        // when
        final DevicePair actualPair = devicePairRepository.findDevicePairByDeviceId(deviceId);
        // then
        assertEquals(devicePairEntity.getDevicePairId(), actualPair.getDeviceId().getValue());
    }

    @Test
    void shouldUpdate() {
        // given
        final DevicePairId devicePairId = new DevicePairId(1L);
        final DevicePair devicePair = new DevicePair(devicePairId, null, false, null, null, null);
        // when
        devicePairRepository.update(devicePair);
        // then
        verify(repository).save(any());
    }
}
