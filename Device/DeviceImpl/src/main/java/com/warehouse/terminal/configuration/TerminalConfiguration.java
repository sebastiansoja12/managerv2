package com.warehouse.terminal.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.domain.port.primary.DevicePairPort;
import com.warehouse.terminal.domain.port.primary.DevicePairPortImpl;
import com.warehouse.terminal.domain.port.primary.DevicePort;
import com.warehouse.terminal.domain.port.primary.DevicePortImpl;
import com.warehouse.terminal.domain.port.secondary.*;
import com.warehouse.terminal.domain.service.*;
import com.warehouse.terminal.infrastructure.adapter.primary.DeviceServiceAdapter;
import com.warehouse.terminal.infrastructure.adapter.secondary.*;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.TerminalEntity;


@Configuration
public class TerminalConfiguration {

    @Bean
    public DevicePairPort terminalPairPort(final DeviceGenericService deviceGenericService,
                                           final UserService userService,
                                           final DevicePairService devicePairService,
                                           final DeviceVersionService deviceVersionService,
                                           final DevicePairValidationService devicePairValidationService,
                                           final DeviceActivationService deviceActivationService,
                                           final OperatorContext operatorContext) {
        return new DevicePairPortImpl(deviceGenericService, userService, devicePairService, deviceVersionService,
                devicePairValidationService, deviceActivationService, operatorContext);
    }

    @Bean
    public DeviceActivationService deviceActivationService(
            final DeviceActivationCodeRepository deviceActivationCodeRepository,
            @Value("${device.activation.code-validity:PT15M}") final Duration activationCodeValidity) {
        return new DeviceActivationServiceImpl(deviceActivationCodeRepository, activationCodeValidity);
    }

    @Bean
    public DeviceActivationCodeRepository deviceActivationCodeRepository(
            final DeviceActivationCodeReadRepository deviceActivationCodeReadRepository) {
        return new DeviceActivationCodeRepositoryImpl(deviceActivationCodeReadRepository);
    }

    @Bean
    public DevicePairKeyService devicePairKeyService(@Value("${device.pair.key}") final String pairKeySecret) {
        return new DevicePairKeyService(pairKeySecret);
    }

    @Bean
    public DeviceValidatorService terminalValidatorService(final DeviceVersionRepository deviceVersionRepository,
                                                           final DeviceGenericRepository deviceRepository,
                                                           final DepartmentServicePort departmentServicePort,
                                                           final UserServicePort userServicePort) {
        return new DeviceValidatorServiceImpl(deviceVersionRepository, deviceRepository, departmentServicePort,
                userServicePort);
    }

    @Bean("terminal.supplierRepository")
    public SupplierRepository supplierRepository(final SupplierReadRepository repository) {
        return new SupplierRepositoryImpl(repository);
    }

    @Bean
    public DepartmentRepository departmentRepository(final DepartmentServicePort departmentServicePort) {
        return new DepartmentRepositoryImpl(departmentServicePort);
    }

    @Bean
    public DeviceVersionRepository deviceVersionRepository(final OperatorFilteredRepository<TerminalEntity> terminalRepository,
                                                           final OperatorFilteredRepository<MobileEntity> mobileRepository,
                                                           final OperatorFilteredRepository<ScannerEntity> scannerRepository,
                                                           final DeviceVersionReadRepository deviceVersionReadRepository) {
        return new DeviceVersionRepositoryImpl(terminalRepository, mobileRepository, scannerRepository,
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
    public UserRepository userRepository(final UserServicePort userServicePort) {
        return new UserRepositoryImpl(userServicePort);
    }

    @Bean
    public DevicePairService devicePairService(
            final DevicePairRepository devicePairRepository,
            final DevicePairKeyService devicePairKeyService,
            @Value("${device.activation.pair-key-validity:P180D}") final Duration activatedPairKeyValidity,
            @Value("${device.operator-session.validity:PT12H}") final Duration operatorSessionValidity) {
        return new DevicePairServiceImpl(devicePairRepository, devicePairKeyService, activatedPairKeyValidity,
                operatorSessionValidity);
    }

    @Bean
    public DevicePairValidationService devicePairValidationService(final UserService userService) {
        return new DevicePairValidationServiceImpl(userService);
    }

    @Bean
    public DevicePairRepository devicePairRepository(final DevicePairReadRepository devicePairReadRepository) {
        return new DevicePairRepositoryImpl(devicePairReadRepository);
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
                                             final UserService userService,
                                             final DepartmentRepository departmentRepository,
                                             final OperatorContext operatorContext) {
        return new DeviceServiceAdapter(devicePairService, deviceGenericService, userService, departmentRepository,
                operatorContext);
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
