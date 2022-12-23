package com.warehouse.route.configuration;

import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.port.secondary.RouteLogServiceImpl;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.domain.port.secondary.RouteTrackerServicePort;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapper;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapperImpl;
import com.warehouse.route.infrastructure.adapter.secondary.*;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteMapper;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteModelMapper;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class  RouteConfiguration {

    @Bean
    public RouteRepository routeRepository(RouteReadRepository routeReadRepository,
                                           RouteDepotReadRepository routeDepotReadRepository,
                                           ParcelReadRepository parcelReadRepository,
                                           SupplierReadRepository supplierReadRepository,
                                           UserReadRepository userReadRepository) {
        final RouteModelMapper routeModelMapper = Mappers.getMapper(RouteModelMapper.class);
        return new RouteRepositoryImpl(routeReadRepository, routeModelMapper, routeDepotReadRepository,
                parcelReadRepository, supplierReadRepository, userReadRepository);
    }

    @Bean
    public RouteTrackerLogPort routeTrackerLogPort(RouteLogService routeLogService,
                                                   RouteTrackerServicePort trackerLogPort) {
        return new RouteTrackerLogPortImpl(routeLogService, trackerLogPort);
    }

    @Bean
    public RouteLogService routeLogService(RouteRepository routeRepository) {
        return new RouteLogServiceImpl(routeRepository);
    }
    @Bean
    public RouteLogEventPublisher routeLogEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new RouteLogEventPublisherImpl(eventPublisher);
    }

    @Bean
    public EventMapper eventMapper() {
        return new EventMapperImpl();
    }

    @Bean
    public RouteTrackerServicePort routeTrackerServicePort(RouteRepository routeRepository, AuthenticationPort authPort) {
        final RouteMapper routeMapper = Mappers.getMapper(RouteMapper.class);
        return new RouteLogAdapter(routeMapper, routeRepository, authPort);
    }
}
