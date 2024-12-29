package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.configuration.DeviceTestConfiguration;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeviceTestConfiguration.class)
@Sql("/dataset/devices.sql")
public class DevicePairReadRepositoryTest {

    @Autowired
    private DevicePairReadRepository repository;

    @Test
    void shouldFindByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        // when
        final Optional<DevicePairEntity> devicePair = repository.findByDevice_DeviceId(deviceId);
        // then
        assertTrue(devicePair.isPresent());
    }

    @Test
    void shouldNotFindByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(10L);
        // when
        final Optional<DevicePairEntity> devicePair = repository.findByDevice_DeviceId(deviceId);
        // then
        assertFalse(devicePair.isPresent());
    }
}
