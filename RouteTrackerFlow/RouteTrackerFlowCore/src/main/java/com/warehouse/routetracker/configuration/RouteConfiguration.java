package com.warehouse.routetracker.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.ParcelStatusUpdateRepository;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.*;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;
import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;

@Configuration
public class RouteConfiguration {

	@Bean
	public RouteRepository routeRepository(RouteReadRepository routeReadRepository) {
		final RouteModelMapper routeModelMapper = Mappers.getMapper(RouteModelMapper.class);
		return new RouteRepositoryImpl(routeReadRepository, routeModelMapper);
	}

    @Bean
    public RouteLogEventPublisher routeLogEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new RouteLogEventPublisherImpl(applicationEventPublisher);
    }

	@Bean
	public RouteTrackerLogPort routeTrackerLogPort(RouteRepository repository,
			ParcelStatusUpdateRepository updateRepository) {
		return new RouteTrackerLogPortImpl(repository, updateRepository);
	}

    @Bean
    public ParcelStatusUpdateRepository parcelStatusUpdateRepository(ParcelReadRepository parcelReadRepository) {
        return new ParcelStatusUpdateRepositoryImpl(parcelReadRepository);
    }
}
