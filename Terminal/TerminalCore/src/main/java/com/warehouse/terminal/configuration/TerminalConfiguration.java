package com.warehouse.terminal.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.port.primary.DevicePortImpl;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.infrastructure.adapter.secondary.*;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;

import io.github.resilience4j.retry.RetryConfig;

import java.time.Duration;

@Configuration
public class TerminalConfiguration {

    @Bean
    public DevicePairPort terminalPairPort(final DeviceValidatorService deviceValidatorService,
                                           final DeviceGenericService deviceGenericService,
                                           final UserService userService,
                                           final DevicePairService devicePairService,
                                           final DeviceVersionService deviceVersionService) {
        return new DevicePairPortImpl(deviceValidatorService, deviceGenericService, userService, devicePairService,
                deviceVersionService);
    }

    @Bean
    public DeviceValidatorService terminalValidatorService(final DeviceVersionRepository deviceVersionRepository,
                                                           final DepartmentRepository departmentRepository,
                                                           final UserRepository userRepository,
                                                           final SupplierRepository supplierRepository,
                                                           final DeviceRepository deviceRepository) {
        return new DeviceValidatorServiceImpl(deviceVersionRepository, departmentRepository,
                userRepository, supplierRepository, deviceRepository);
    }

    @Bean("device.softwareConfigurationServicePort")
    @ConditionalOnProperty(name = "services.mock", havingValue = "false")
    public SoftwareConfigurationServicePort softwareConfigurationServicePort(final SoftwareConfigurationProperties softwareConfigurationProperties) {
        final RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(4)
                .waitDuration(Duration.ofSeconds(2))
                .retryExceptions(RuntimeException.class)
                .writableStackTraceEnabled(true)
                .build();
        return new SoftwareConfigurationServiceAdapter(retryConfig, softwareConfigurationProperties);
    }

    @Bean("device.softwareConfigurationServicePort")
    @ConditionalOnProperty(name = "services.mock", havingValue = "true")
    public SoftwareConfigurationServicePort softwareConfigurationServiceMockPort() {
        return new SoftwareConfigurationServiceMockAdapter();
    }

    @Bean("device.softwareConfigurationProperties")
    public SoftwareConfigurationProperties softwareConfigurationProperties() {
        return new SoftwareConfigurationProperties();
    }

    @Bean("terminal.supplierRepository")
    public SupplierRepository supplierRepository(final SupplierReadRepository repository) {
        return new SupplierRepositoryImpl(repository);
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
    public DeviceGenericService terminalService(final DeviceRepository deviceRepository,
                                                final DeviceSettingsRepository deviceSettingsRepository) {
        return new DeviceGenericServiceImpl(deviceRepository, deviceSettingsRepository);
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
    public DevicePairRepository devicePairRepository(final DevicePairReadRepository devicePairReadRepository,
                                                     final DeviceRepository repository) {
        return new DevicePairRepositoryImpl(devicePairReadRepository, repository);
    }

    @Bean
    public DevicePort terminalPort(final DeviceGenericService deviceGenericService,
                                   final UserService userService,
                                   final DeviceVersionService deviceVersionService) {
        return new DevicePortImpl(deviceGenericService, userService, deviceVersionService);
    }

    @Bean
    public DeviceSettingsRepository deviceSettingsRepository(final DeviceSettingsReadRepository deviceSettingsReadRepository) {
        return new DeviceSettingsRepositoryImpl(deviceSettingsReadRepository);
    }
}
