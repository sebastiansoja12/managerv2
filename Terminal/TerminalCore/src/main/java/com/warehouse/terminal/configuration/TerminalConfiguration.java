package com.warehouse.terminal.configuration;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.port.primary.DevicePortImpl;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.infrastructure.adapter.primary.DeviceServiceAdapter;
import com.warehouse.terminal.infrastructure.adapter.secondary.*;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;

import io.github.resilience4j.retry.RetryConfig;

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
                                                           final DeviceGenericRepository deviceRepository,
                                                           final DepartmentServicePort departmentServicePort,
                                                           final UserServicePort userServicePort) {
        return new DeviceValidatorServiceImpl(deviceVersionRepository, deviceRepository, departmentServicePort,
                userServicePort);
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
    @ConditionalOnProperty(name = "services.mock", havingValue = "true", matchIfMissing = true)
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
    public DeviceVersionRepository deviceVersionRepository(final TerminalReadRepository terminalReadRepository,
                                                           final MobileReadRepository mobileReadRepository,
                                                           final ScannerReadRepository scannerReadRepository,
                                                           final DeviceVersionReadRepository deviceVersionReadRepository) {
        return new DeviceVersionRepositoryImpl(terminalReadRepository, mobileReadRepository, scannerReadRepository,
                deviceVersionReadRepository);
    }

    @Bean
    public DeviceVersionService deviceVersionService(final DeviceVersionRepository deviceVersionRepository) {
        return new DeviceVersionServiceImpl(deviceVersionRepository);
    }

    @Bean
    public DeviceGenericService terminalService(final DeviceGenericRepository deviceGenericRepository,
                                                final DeviceSettingsRepository deviceSettingsRepository) {
        return new DeviceGenericServiceImpl(deviceGenericRepository, deviceSettingsRepository);
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
                                                     final DeviceRepository<Terminal> terminalRepository) {
        return new DevicePairRepositoryImpl(devicePairReadRepository, terminalRepository);
    }

    @Bean
    public DevicePort terminalPort(final DeviceGenericService deviceGenericService,
                                   final UserService userService,
                                   final DepartmentServicePort departmentServicePort,
                                   final UserServicePort userServicePort) {
        return new DevicePortImpl(deviceGenericService, userService, departmentServicePort, userServicePort);
    }

    @Bean
    public UserServicePort userServicePort(final UserApiService userApiService) {
        return new UserServiceAdapter(userApiService);
    }

    @Bean
    public DeviceApiService deviceApiService(final DevicePairService devicePairService,
                                             final DeviceGenericService deviceGenericService,
                                             final UserService userService) {
        return new DeviceServiceAdapter(devicePairService, deviceGenericService, userService);
    }

    @Bean
    public DepartmentServicePort departmentServicePort(final DepartmentApiService departmentApiService) {
        return new DepartmentServiceAdapter(departmentApiService);
    }

    @Bean
    public DeviceSettingsRepository deviceSettingsRepository(final DeviceSettingsReadRepository deviceSettingsReadRepository) {
        return new DeviceSettingsRepositoryImpl(deviceSettingsReadRepository);
    }
}
