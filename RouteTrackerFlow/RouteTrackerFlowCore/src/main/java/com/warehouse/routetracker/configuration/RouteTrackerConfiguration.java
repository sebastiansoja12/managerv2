package com.warehouse.routetracker.configuration;

import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.RouteLogEventPublisher;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.service.RouteLoggerService;
import com.warehouse.routetracker.domain.service.RouteLoggerServiceImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteTrackerRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteTrackerReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteTrackerRepositoryImpl;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;

@Configuration
public class RouteTrackerConfiguration {

    @Bean
    public RouteTrackerRepository routeRepository(RouteTrackerReadRepository routeTrackerReadRepository) {
        final RouteModelMapper routeModelMapper = Mappers.getMapper(RouteModelMapper.class);
        return new RouteTrackerRepositoryImpl(routeTrackerReadRepository, routeModelMapper);
    }

    @Bean
    public RouteLogEventPublisher routeLogEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new RouteLogEventPublisherImpl(applicationEventPublisher);
    }

    @Bean
    public RouteTrackerLogPort routeTrackerLogPort(RouteLoggerService routeLoggerService) {
        return new RouteTrackerLogPortImpl(routeLoggerService);
    }

    @Bean
    public RouteLoggerService routeLogService(RouteTrackerRepository routeTrackerRepository) {
        return new RouteLoggerServiceImpl(routeTrackerRepository);
    }

}
