package com.warehouse.terminal;


import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.terminal.configuration.DeviceTestConfiguration;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceVersionReadRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeviceTestConfiguration.class)
@Sql("/dataset/devices.sql")
public class DeviceVersionReadRepositoryTest {

    @Autowired
    private DeviceVersionReadRepository repository;

    @Test
    void shouldFindByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(1L);
        // when
        final Optional<DeviceVersionEntity> deviceVersion = repository.findByDeviceId(deviceId);
        // then
        assertTrue(deviceVersion.isPresent());
    }

    @Test
    void shouldNotFindByDeviceId() {
        // given
        final DeviceId deviceId = new DeviceId(10L);
        // when
        final Optional<DeviceVersionEntity> deviceVersion = repository.findByDeviceId(deviceId);
        // then
        assertTrue(deviceVersion.isEmpty());
    }
}
