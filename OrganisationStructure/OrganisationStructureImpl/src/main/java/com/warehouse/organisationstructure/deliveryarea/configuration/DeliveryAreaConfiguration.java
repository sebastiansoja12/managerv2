package com.warehouse.organisationstructure.deliveryarea.configuration;

import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPort;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPortImpl;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.CountryBoundaryServicePort;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaConfigurationServicePort;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaDepartmentServicePort;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaRepository;
import com.warehouse.organisationstructure.deliveryarea.domain.service.DeliveryAreaGenerator;
import com.warehouse.organisationstructure.deliveryarea.api.DeliveryAreaApiService;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.DeliveryAreaServiceAdapter;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.DepartmentCreatedEventListener;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.DepartmentDeletedEventListener;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.DepartmentStatusChangedEventListener;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.mapper.DeliveryAreaApiMapper;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.CountryBoundaryServiceAdapter;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.DeliveryAreaConfigurationServiceAdapter;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.DeliveryAreaDepartmentServiceAdapter;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.DeliveryAreaRepositoryAdapter;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.entity.DeliveryAreaEntity;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.mapper.DeliveryAreaPersistenceMapper;
import com.warehouse.organisationstructure.department.application.port.primary.DepartmentPort;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;

@Configuration
public class DeliveryAreaConfiguration {

    @Bean
    public DeliveryAreaPort deliveryAreaPort(final DeliveryAreaRepository deliveryAreaRepository,
                                             final DeliveryAreaDepartmentServicePort deliveryAreaDepartmentServicePort,
                                             final DeliveryAreaConfigurationServicePort deliveryAreaConfigurationServicePort,
                                             final CountryBoundaryServicePort countryBoundaryServicePort) {
        return new DeliveryAreaPortImpl(deliveryAreaRepository, deliveryAreaDepartmentServicePort,
                deliveryAreaConfigurationServicePort, new DeliveryAreaGenerator(countryBoundaryServicePort.countryBoundary()), Clock.systemUTC());
    }

    @Bean
    public BaseRepository<DeliveryAreaEntity> deliveryAreaEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DeliveryAreaRepository deliveryAreaRepository(final BaseRepository<DeliveryAreaEntity> deliveryAreaEntityRepository) {
        return new DeliveryAreaRepositoryAdapter(deliveryAreaEntityRepository, new DeliveryAreaPersistenceMapper());
    }

    @Bean
    public DeliveryAreaDepartmentServicePort deliveryAreaDepartmentServicePort(final DepartmentPort departmentPort) {
        return new DeliveryAreaDepartmentServiceAdapter(departmentPort);
    }

    @Bean
    public DeliveryAreaConfigurationServicePort deliveryAreaConfigurationServicePort(
            final OperatorConfigurationPort operatorConfigurationPort) {
        return new DeliveryAreaConfigurationServiceAdapter(operatorConfigurationPort);
    }

    @Bean
    public DeliveryAreaApiService deliveryAreaApiService(final DeliveryAreaPort deliveryAreaPort) {
        return new DeliveryAreaServiceAdapter(deliveryAreaPort);
    }

    @Bean
    public DepartmentCreatedEventListener deliveryAreaDepartmentCreatedEventListener(
            final DeliveryAreaPort deliveryAreaPort) {
        return new DepartmentCreatedEventListener(deliveryAreaPort);
    }

    @Bean
    public DepartmentDeletedEventListener deliveryAreaDepartmentDeletedEventListener(
            final DeliveryAreaPort deliveryAreaPort) {
        return new DepartmentDeletedEventListener(deliveryAreaPort);
    }

    @Bean
    public DepartmentStatusChangedEventListener deliveryAreaDepartmentStatusChangedEventListener(
            final DeliveryAreaPort deliveryAreaPort) {
        return new DepartmentStatusChangedEventListener(deliveryAreaPort);
    }

    @Bean
    public CountryBoundaryServicePort countryBoundaryServicePort(final ObjectMapper objectMapper) {
        return new CountryBoundaryServiceAdapter(objectMapper);
    }

    @Bean
    public DeliveryAreaApiMapper deliveryAreaApiMapper() {
        return new DeliveryAreaApiMapper();
    }
}
