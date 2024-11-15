package com.warehouse.terminal.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.port.primary.TerminalPort;
import com.warehouse.terminal.domain.port.primary.TerminalPortImpl;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.infrastructure.adapter.secondary.*;

@Configuration
public class TerminalConfiguration {

    @Bean
    public DevicePairPort terminalPairPort(final TerminalValidatorService terminalValidatorService,
                                           final TerminalService terminalService,
                                           final UserService userService,
                                           final DevicePairService devicePairService,
                                           final DeviceVersionService deviceVersionService) {
        return new DevicePairPortImpl(terminalValidatorService, terminalService, userService, devicePairService,
                deviceVersionService);
    }

    @Bean
    public TerminalValidatorService terminalValidatorService(final DeviceVersionRepository deviceVersionRepository,
                                                             final DepartmentRepository departmentRepository) {
        return new TerminalValidatorServiceImpl(deviceVersionRepository, departmentRepository);
    }

    @Bean
    public DepartmentRepository departmentRepository(final DepartmentReadRepository repository) {
        return new DepartmentRepositoryImpl(repository);
    }

    @Bean
    public DeviceVersionRepository deviceVersionRepository(final DeviceReadRepository deviceReadRepository,
                                                           final DeviceVersionReadRepository deviceVersionReadRepository) {
        return new DeviceVersionRepositoryImpl(deviceReadRepository, deviceVersionReadRepository);
    }

    @Bean
    public DeviceVersionService deviceVersionService(final DeviceVersionRepository deviceVersionRepository) {
        return new DeviceVersionServiceImpl(deviceVersionRepository);
    }

    @Bean
    public TerminalService terminalService(final DeviceRepository deviceRepository) {
        return new TerminalServiceImpl(deviceRepository);
    }

    @Bean
    public DeviceRepository deviceRepository(final DeviceReadRepository deviceReadRepository) {
        return new DeviceRepositoryImpl(deviceReadRepository);
    }

    @Bean("device.userService")
    public UserService userService(final UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean("device.userRepository")
    public UserRepository userRepository(final UserReadRepository repository) {
        return new UserRepositoryImpl(repository);
    }

    @Bean
    public DevicePairService devicePairService(final DevicePairRepository devicePairRepository) {
        return new DevicePairServiceImpl(devicePairRepository);
    }

    @Bean
    public DevicePairRepository devicePairRepository(final DevicePairReadRepository devicePairReadRepository) {
        return new DevicePairRepositoryImpl(devicePairReadRepository);
    }

    @Bean
    public TerminalPort terminalPort(final TerminalService terminalService,
                                     final UserService userService) {
        return new TerminalPortImpl(terminalService, userService);
    }
}
