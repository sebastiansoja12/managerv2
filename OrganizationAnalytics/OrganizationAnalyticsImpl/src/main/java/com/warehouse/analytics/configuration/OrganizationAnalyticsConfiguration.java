package com.warehouse.analytics.configuration;

import com.warehouse.analytics.application.port.primary.DepartmentCostPort;
import com.warehouse.analytics.application.port.primary.DepartmentCostPortImpl;
import com.warehouse.analytics.application.port.primary.OrganizationAnalyticsPort;
import com.warehouse.analytics.application.port.primary.OrganizationAnalyticsPortImpl;
import com.warehouse.analytics.application.port.secondary.AnalyticsUserServicePort;
import com.warehouse.analytics.application.port.secondary.DepartmentCostRepository;
import com.warehouse.analytics.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.analytics.application.port.secondary.IncidentStatisticsServicePort;
import com.warehouse.analytics.application.port.secondary.ShipmentStatisticsServicePort;
import com.warehouse.analytics.domain.service.DepartmentCostFactory;
import com.warehouse.analytics.domain.service.DepartmentPerformanceCalculator;
import com.warehouse.analytics.domain.service.ShipmentVolumeForecaster;
import com.warehouse.analytics.infrastructure.adapter.primary.mapper.AnalyticsWebMapper;
import com.warehouse.analytics.infrastructure.adapter.secondary.AnalyticsUserServiceAdapter;
import com.warehouse.analytics.infrastructure.adapter.secondary.DepartmentCostRepositoryImpl;
import com.warehouse.analytics.infrastructure.adapter.secondary.DepartmentDirectoryServiceAdapter;
import com.warehouse.analytics.infrastructure.adapter.secondary.IncidentStatisticsServiceAdapter;
import com.warehouse.analytics.infrastructure.adapter.secondary.ShipmentStatisticsServiceAdapter;
import com.warehouse.analytics.infrastructure.adapter.secondary.entity.DepartmentCostEntity;
import com.warehouse.analytics.infrastructure.adapter.secondary.mapper.DepartmentCostPersistenceMapper;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.incident.api.IncidentStatisticsApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.shipment.infrastructure.ShipmentStatisticsApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class OrganizationAnalyticsConfiguration {

    @Bean("departmentCostBaseRepository")
    public OperatorFilteredRepository<DepartmentCostEntity> departmentCostBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DepartmentCostRepository departmentCostRepository(
            @Qualifier("departmentCostBaseRepository")
            final OperatorFilteredRepository<DepartmentCostEntity> repository) {
        return new DepartmentCostRepositoryImpl(repository, new DepartmentCostPersistenceMapper());
    }

    @Bean
    public ShipmentStatisticsServicePort analyticsShipmentStatisticsServicePort(
            final ShipmentStatisticsApiService shipmentStatisticsApiService) {
        return new ShipmentStatisticsServiceAdapter(shipmentStatisticsApiService);
    }

    @Bean
    public IncidentStatisticsServicePort analyticsIncidentStatisticsServicePort(
            final IncidentStatisticsApiService incidentStatisticsApiService) {
        return new IncidentStatisticsServiceAdapter(incidentStatisticsApiService, Clock.systemDefaultZone().getZone());
    }

    @Bean
    public DepartmentDirectoryServicePort analyticsDepartmentDirectoryServicePort(
            final DepartmentApiService departmentApiService) {
        return new DepartmentDirectoryServiceAdapter(departmentApiService);
    }

    @Bean
    public AnalyticsUserServicePort analyticsUserServicePort(final OperatorContextProvider operatorContextProvider) {
        return new AnalyticsUserServiceAdapter(operatorContextProvider);
    }

    @Bean
    public OrganizationAnalyticsPort organizationAnalyticsPort(
            final ShipmentStatisticsServicePort analyticsShipmentStatisticsServicePort,
            final IncidentStatisticsServicePort analyticsIncidentStatisticsServicePort,
            final DepartmentDirectoryServicePort analyticsDepartmentDirectoryServicePort,
            final DepartmentCostRepository departmentCostRepository) {
        return new OrganizationAnalyticsPortImpl(analyticsShipmentStatisticsServicePort,
                analyticsIncidentStatisticsServicePort, analyticsDepartmentDirectoryServicePort,
                departmentCostRepository, new DepartmentPerformanceCalculator(), new ShipmentVolumeForecaster(),
                Clock.systemDefaultZone());
    }

    @Bean
    public DepartmentCostPort departmentCostPort(
            final DepartmentCostRepository departmentCostRepository,
            final DepartmentDirectoryServicePort analyticsDepartmentDirectoryServicePort,
            final AnalyticsUserServicePort analyticsUserServicePort) {
        return new DepartmentCostPortImpl(departmentCostRepository, analyticsDepartmentDirectoryServicePort,
                analyticsUserServicePort, new DepartmentCostFactory(Clock.systemDefaultZone()));
    }

    @Bean
    public AnalyticsWebMapper analyticsWebMapper() {
        return new AnalyticsWebMapper();
    }
}
