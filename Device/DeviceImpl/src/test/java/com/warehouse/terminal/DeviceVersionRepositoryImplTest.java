package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceVersionReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceVersionRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;

class DeviceVersionRepositoryImplTest {

    private final DeviceVersionReadRepository readRepository = mock(DeviceVersionReadRepository.class);

    @SuppressWarnings("unchecked")
    private final DeviceVersionRepositoryImpl repository = new DeviceVersionRepositoryImpl(
            mock(OperatorFilteredRepository.class), mock(OperatorFilteredRepository.class),
            mock(OperatorFilteredRepository.class), readRepository);

    @Test
    void shouldUpdateExistingVersionRowInsteadOfInsertingNewOne() {
        final DeviceVersionEntity existing = new DeviceVersionEntity(7L, DeviceType.MOBILE, "1.0.0",
                DeviceAccessFixture.DEVICE_ID);
        when(readRepository.findFirstByDeviceIdOrderByIdDesc(DeviceAccessFixture.DEVICE_ID))
                .thenReturn(Optional.of(existing));
        final ArgumentCaptor<DeviceVersionEntity> saved = ArgumentCaptor.forClass(DeviceVersionEntity.class);

        repository.saveOrUpdate(new DeviceVersion(DeviceType.MOBILE, "1.1.0", DeviceAccessFixture.DEVICE_ID));

        verify(readRepository).save(saved.capture());
        assertSame(existing, saved.getValue());
        assertEquals(7L, saved.getValue().getId());
        assertEquals("1.1.0", saved.getValue().getVersion());
    }

    @Test
    void shouldCreateVersionRowForNewDevice() {
        when(readRepository.findFirstByDeviceIdOrderByIdDesc(DeviceAccessFixture.DEVICE_ID)).thenReturn(Optional.empty());
        final ArgumentCaptor<DeviceVersionEntity> saved = ArgumentCaptor.forClass(DeviceVersionEntity.class);

        repository.saveOrUpdate(new DeviceVersion(DeviceType.MOBILE, "1.0.0", DeviceAccessFixture.DEVICE_ID));

        verify(readRepository).save(saved.capture());
        assertEquals("1.0.0", saved.getValue().getVersion());
        assertEquals(DeviceAccessFixture.DEVICE_ID, saved.getValue().getDeviceId());
    }
}
