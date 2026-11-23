package com.warehouse.supplier.configuration;

import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.department.api.DepartmentLifecycleParticipant;
import com.warehouse.supplier.application.port.secondary.CourierLimitServicePort;
import com.warehouse.supplier.infrastructure.adapter.primary.SupplierDepartmentLifecycleParticipant;
import com.warehouse.supplier.infrastructure.adapter.secondary.CourierLimitServiceAdapter;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.SupplierUserEventPublisher;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.eventoutbox.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.infrastructure.adapter.primary.SupplierApiServiceAdapter;
import com.warehouse.supplier.infrastructure.adapter.primary.SupplierValidityJob;
import com.warehouse.supplier.application.port.primary.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.supplier.application.port.secondary.*;
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
    public SupplyPort supplyPort(final SupplierRepository supplierRepository,
                                 final SupplierCodeGeneratorService supplierCodeGeneratorService,
                                 final DriverLicenseService driverLicenseService,
                                 @Qualifier("supplier.departmentServicePort") final DepartmentServicePort departmentServicePort,
                                 @Qualifier("supplier.userServicePort") final UserServicePort userServicePort,
                                 final CourierLimitServicePort courierLimitServicePort,
                                 @Qualifier("supplier.currentUserServicePort") final CurrentUserServicePort currentUserServicePort,
                                 final DomainEventPublisher domainEventPublisher) {
        return new SupplyPortImpl(supplierRepository, supplierCodeGeneratorService, driverLicenseService,
                departmentServicePort, userServicePort, courierLimitServicePort, currentUserServicePort,
                domainEventPublisher);
    }

    @Bean("supplier.currentUserServicePort")
    public CurrentUserServicePort supplierCurrentUserServicePort(final CurrentUserApiService currentUserApiService) {
        return new CurrentUserServiceAdapter(currentUserApiService);
    }

    @Bean
    public CourierLimitServicePort courierLimitServicePort(
            final OperatorConfigurationApiService operatorConfigurationApiService) {
        return new CourierLimitServiceAdapter(operatorConfigurationApiService);
    }

    @Bean
    public DepartmentLifecycleParticipant supplierDepartmentLifecycleParticipant(final SupplyPort supplyPort) {
        return new SupplierDepartmentLifecycleParticipant(supplyPort);
    }

    @Bean("supplier.userServicePort")
    public UserServicePort userServicePort(final SupplierUserEventPublisher supplierUserEventPublisher) {
        return new UserServiceAdapter(supplierUserEventPublisher);
    }

    @Bean
    public SupplierApiService supplierApiService(final SupplyPort supplyPort) {
        return new SupplierApiServiceAdapter(supplyPort);
    }

    @Bean
    public DriverLicenseObserverPort driverLicenseObserverPort(
            final DriverLicenseService driverLicenseService,
            final OperatorServicePort operatorServicePort,
            final SupplierRepository supplierRepository,
            final OperatorContext operatorContext,
            final MailServicePort mailServicePort) {
        return new DriverLicenseObserverPortImpl(driverLicenseService, operatorServicePort, supplierRepository,
                operatorContext, mailServicePort);
    }

    @Bean
    public SupplierValidityJob supplierValidityJob(final DriverLicenseObserverPort driverLicenseObserverPort,
                                                   final DangerousGoodObserverPort dangerousGoodObserverPort) {
        return new SupplierValidityJob(driverLicenseObserverPort, dangerousGoodObserverPort);
    }

    @Bean
    public MailServicePort supplierMailServicePort(final IntegrationEventPublisher integrationEventPublisher) {
        return new MailServiceAdapter(integrationEventPublisher);
    }

    @Bean
    public OperatorServicePort supplierOperatorServicePort(final OperatorApiService operatorApiService) {
        return new OperatorServiceAdapter(operatorApiService);
    }

    @Bean
    public DangerousGoodObserverPort dangerousGoodObserverPort(final OperatorServicePort operatorServicePort,
                                                               final SupplierRepository supplierRepository,
                                                               final OperatorContext operatorContext) {
        return new DangerousGoodObserverPortImpl(operatorServicePort, supplierRepository, operatorContext);
    }

    @Bean
    public DriverLicenseService driverLicenseService() {
        return new DriverLicenseServiceImpl();
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
    public BaseRepository<SupplierEntity> supplierBaseRepository(final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean(name = "supplier.departmentServicePort")
    public DepartmentServicePort departmentServicePort(final DepartmentApiService departmentApiService) {
        return new DepartmentServiceAdapter(departmentApiService);
    }
}
