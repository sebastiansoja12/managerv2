package com.warehouse.fleettracking.configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.fleettracking.application.port.primary.FleetTrackingPort;
import com.warehouse.fleettracking.application.port.primary.FleetTrackingPortImpl;
import com.warehouse.fleettracking.application.port.secondary.TrackingDriverServicePort;
import com.warehouse.fleettracking.application.port.secondary.TrackingSessionRepository;
import com.warehouse.fleettracking.infrastructure.adapter.primary.mapper.FleetTrackingWebMapper;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.TrackingDriverServiceAdapter;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.TrackingSessionRepositoryImpl;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.TrackingPositionEntity;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.entity.TrackingSessionEntity;
import com.warehouse.fleettracking.infrastructure.adapter.secondary.mapper.TrackingSessionPersistenceMapper;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class FleetTrackingConfiguration {

    @Bean("trackingSessionBaseRepository")
    public OperatorFilteredRepository<TrackingSessionEntity> trackingSessionBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("trackingPositionBaseRepository")
    public OperatorFilteredRepository<TrackingPositionEntity> trackingPositionBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public TrackingSessionRepository trackingSessionRepository(
            @Qualifier("trackingSessionBaseRepository")
            final OperatorFilteredRepository<TrackingSessionEntity> sessionRepository,
            @Qualifier("trackingPositionBaseRepository")
            final OperatorFilteredRepository<TrackingPositionEntity> positionRepository) {
        return new TrackingSessionRepositoryImpl(sessionRepository, positionRepository,
                new TrackingSessionPersistenceMapper());
    }

    @Bean
    public TrackingDriverServicePort trackingDriverServicePort(final OperatorContextProvider operatorContextProvider,
                                                               final UserApiService userApiService,
                                                               final DepartmentApiService departmentApiService) {
        return new TrackingDriverServiceAdapter(operatorContextProvider, userApiService, departmentApiService);
    }

    @Bean
    public FleetTrackingPort fleetTrackingPort(final TrackingSessionRepository trackingSessionRepository,
                                               final TrackingDriverServicePort trackingDriverServicePort) {
        return new FleetTrackingPortImpl(trackingSessionRepository, trackingDriverServicePort,
                Clock.systemDefaultZone());
    }

    @Bean
    public FleetTrackingWebMapper fleetTrackingWebMapper() {
        return new FleetTrackingWebMapper();
    }
}
