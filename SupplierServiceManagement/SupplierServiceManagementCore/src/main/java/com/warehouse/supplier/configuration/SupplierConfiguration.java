package com.warehouse.supplier.configuration;

import com.warehouse.supplier.domain.port.primary.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.supplier.domain.port.secondary.*;
import com.warehouse.supplier.domain.service.*;
import com.warehouse.supplier.infrastructure.adapter.secondary.*;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class SupplierConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public SupplyPort supplyPort(final SupplierService service,
                                 final SupplierCodeGeneratorService generatorService,
                                 final SupplierValidatorService validatorService,
                                 final DriverLicenseService driverLicenseService,
                                 final DeviceServicePort deviceServicePort) {
        return new SupplyPortImpl(service, generatorService, validatorService, driverLicenseService, deviceServicePort);
    }

    @Bean
    public DriverLicenseObserverPort driverLicenseObserverPort(
            final DriverLicenseService driverLicenseService,
            final SupplierNoDepartmentContextService supplierNoDepartmentContextService
    ) {
        return new DriverLicenseObserverPortImpl(driverLicenseService, supplierNoDepartmentContextService);
    }

    @Bean
    public DangerousGoodObserverPort dangerousGoodObserverPort(final SupplierNoDepartmentContextService service) {
        return new DangerousGoodObserverPortImpl(service);
    }

    @Bean
    public SupplierNoDepartmentContextRepository supplierNoDepartmentContextRepository(
            final SupplierReadRepository supplierReadRepository) {
        return new SupplierNoDepartmentContextRepositoryImpl(supplierReadRepository);
    }

    @Bean("supplier.userServicePort")
    public UserServicePort userServicePort() {
        return new UserServiceAdapter();
    }

    @Bean
    public DeviceServicePort deviceServicePort(final SupplierConfig supplierConfig) {
        return new DeviceServiceAdapter(supplierConfig);
    }

    @Bean
    public DriverLicenseService driverLicenseService() {
        return new DriverLicenseServiceImpl();
    }

    @Bean
    public SupplierService supplierService(final SupplierRepository repository) {
        return new SupplierServiceImpl(repository);
    }

    @Bean
    public SupplierCodeGeneratorService generatorService() {
        return new SupplierCodeGeneratorServiceImpl();
    }

    @Bean
    public SupplierRepository supplierRepository(@Qualifier("supplierBaseRepository") final BaseRepository<SupplierEntity> basePathRepository) {
        return new SupplierRepositoryImpl(basePathRepository);
    }

    @Bean
    public BaseRepository<SupplierEntity> supplierBaseRepository() {
        return new BaseRepository<>(entityManager);
    }

    @Bean("supplier.mailServicePort")
    public MailServicePort mailServicePort() {
        return new MailServiceAdapter();
    }
}
