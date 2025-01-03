package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeviceTestConfiguration.class)
@Sql("/dataset/devices.sql")
public class DeviceReadRepositoryTest {

    @Autowired
    private DeviceReadRepository repository;

    @Test
    void shouldFindByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        // when
        final Optional<DeviceEntity> device = repository.findById(deviceId);
        // then
        assertTrue(device.isPresent());
    }

    @Test
    void shouldNotFindByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(10L);
        // when
        final Optional<DeviceEntity> device = repository.findById(deviceId);
        // then
        assertFalse(device.isPresent());
    }

    @Test
    void shouldFindAll() {
        final List<DeviceEntity> devices = repository.findAll();
        assertFalse(devices.isEmpty());
    }

}
