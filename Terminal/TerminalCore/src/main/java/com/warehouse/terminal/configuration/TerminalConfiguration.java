package com.warehouse.terminal.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.terminal.domain.port.primary.TerminalPairPort;
import com.warehouse.terminal.domain.port.primary.TerminalPairPortImpl;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.infrastructure.adapter.secondary.*;

@Configuration
public class TerminalConfiguration {

    @Bean
    public TerminalPairPort terminalPairPort(final TerminalValidatorService terminalValidatorService,
                                             final TerminalService terminalService,
                                             final UserService userService,
                                             final DevicePairService devicePairService) {
        return new TerminalPairPortImpl(terminalValidatorService, terminalService, userService, devicePairService);
    }

    @Bean
    public TerminalValidatorService terminalValidatorService(final DeviceVersionRepository deviceVersionRepository,
                                                             final DepartmentRepository departmentRepository) {
        return new TerminalValidatorServiceImpl(deviceVersionRepository, departmentRepository);
    }

    @Bean
    public DeviceVersionRepository deviceVersionRepository(final DeviceReadRepository deviceReadRepository,
                                                           final DeviceVersionReadRepository deviceVersionReadRepository) {
        return new DeviceVersionRepositoryImpl(deviceReadRepository, deviceVersionReadRepository);
    }

    @Bean
    public TerminalService terminalService(final DeviceRepository deviceRepository) {
        return new TerminalServiceImpl(deviceRepository);
    }

    @Bean
    public DeviceRepository deviceRepository(final DeviceReadRepository deviceReadRepository) {
        return new DeviceRepositoryImpl(deviceReadRepository);
    }

    @Bean
    public UserService userService(final UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
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
}
