package com.warehouse.terminal.configuration;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.port.primary.DevicePortImpl;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceSettingsRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.terminal.domain.port.secondary.SupplierRepository;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.service.DeviceGenericService;
import com.warehouse.terminal.domain.service.DeviceGenericServiceImpl;
import com.warehouse.terminal.domain.service.DevicePairService;
import com.warehouse.terminal.domain.service.DevicePairServiceImpl;
import com.warehouse.terminal.domain.service.DeviceValidatorService;
import com.warehouse.terminal.domain.service.DeviceValidatorServiceImpl;
import com.warehouse.terminal.domain.service.DeviceVersionService;
import com.warehouse.terminal.domain.service.DeviceVersionServiceImpl;
import com.warehouse.terminal.domain.service.UserService;
import com.warehouse.terminal.domain.service.UserServiceImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.DepartmentReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DepartmentRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.DepartmentServiceAdapter;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceGenericRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DevicePairRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceSettingsReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceSettingsRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceVersionReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.DeviceVersionRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.MobileReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.ScannerReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.SoftwareConfigurationServiceAdapter;
import com.warehouse.terminal.infrastructure.adapter.secondary.SoftwareConfigurationServiceMockAdapter;
import com.warehouse.terminal.infrastructure.adapter.secondary.SupplierReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.SupplierRepositoryImpl;
import com.warehouse.terminal.infrastructure.adapter.secondary.TerminalReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.UserRepositoryImpl;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;
import com.warehouse.terminal.infrastructure.adapter.primary.DeviceServiceAdapter;

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
                                                           final DepartmentRepository departmentRepository,
                                                           final UserRepository userRepository,
                                                           final SupplierRepository supplierRepository,
                                                           final DeviceRepository<Terminal> deviceRepository) {
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
                                   final DeviceVersionService deviceVersionService,
                                   final DepartmentServicePort departmentServicePort) {
        return new DevicePortImpl(deviceGenericService, userService, deviceVersionService, departmentServicePort);
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
