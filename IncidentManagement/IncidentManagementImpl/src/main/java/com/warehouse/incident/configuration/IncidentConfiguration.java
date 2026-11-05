package com.warehouse.incident.configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.incident.application.port.primary.IncidentPort;
import com.warehouse.incident.application.port.primary.IncidentPortImpl;
import com.warehouse.incident.application.port.secondary.IncidentActorServicePort;
import com.warehouse.incident.application.port.secondary.IncidentRepository;
import com.warehouse.incident.application.port.secondary.IncidentShipmentServicePort;
import com.warehouse.incident.infrastructure.adapter.primary.mapper.IncidentWebMapper;
import com.warehouse.incident.infrastructure.adapter.secondary.IncidentActorServiceAdapter;
import com.warehouse.incident.infrastructure.adapter.secondary.IncidentRepositoryImpl;
import com.warehouse.incident.infrastructure.adapter.secondary.IncidentShipmentServiceAdapter;
import com.warehouse.incident.infrastructure.adapter.secondary.entity.IncidentEntity;
import com.warehouse.incident.infrastructure.adapter.secondary.mapper.IncidentPersistenceMapper;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class IncidentConfiguration {

    @Bean("incidentBaseRepository")
    public OperatorFilteredRepository<IncidentEntity> incidentBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public IncidentRepository incidentRepository(
            @Qualifier("incidentBaseRepository")
            final OperatorFilteredRepository<IncidentEntity> repository) {
        return new IncidentRepositoryImpl(repository, new IncidentPersistenceMapper());
    }

    @Bean
    public IncidentActorServicePort incidentActorServicePort(final OperatorContextProvider operatorContextProvider,
                                                             final UserApiService userApiService,
                                                             final DepartmentApiService departmentApiService) {
        return new IncidentActorServiceAdapter(operatorContextProvider, userApiService, departmentApiService);
    }

    @Bean
    public IncidentShipmentServicePort incidentShipmentServicePort(final ShipmentApiService shipmentApiService) {
        return new IncidentShipmentServiceAdapter(shipmentApiService);
    }

    @Bean
    public IncidentPort incidentPort(final IncidentRepository incidentRepository,
                                     final IncidentActorServicePort incidentActorServicePort,
                                     final IncidentShipmentServicePort incidentShipmentServicePort) {
        return new IncidentPortImpl(incidentRepository, incidentActorServicePort, incidentShipmentServicePort,
                Clock.systemDefaultZone());
    }

    @Bean
    public IncidentWebMapper incidentWebMapper() {
        return new IncidentWebMapper();
    }
}
