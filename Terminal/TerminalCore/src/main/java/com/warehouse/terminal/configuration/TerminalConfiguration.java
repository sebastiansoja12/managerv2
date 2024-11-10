package com.warehouse.terminal.configuration;


import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairReadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.terminal.domain.port.primary.TerminalPairPort;
import com.warehouse.terminal.domain.port.primary.TerminalPairPortImpl;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceRepositoryImpl;

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
    public DevicePairService devicePairService(final DevicePairRepository devicePairRepository) {
        return new DevicePairServiceImpl(devicePairRepository);
    }

    @Bean
    public DevicePairRepository devicePairRepository(final DevicePairReadRepository devicePairReadRepository) {
        return new DevicePairRepositoryImpl(devicePairReadRepository);
    }
}
