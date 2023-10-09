package com.warehouse.routetracker.configuration;

import com.warehouse.routetracker.domain.port.secondary.ParcelStatusUpdateRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.*;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;

@Configuration
public class  RouteConfiguration {

    @Bean
    public RouteRepository routeRepository(RouteReadRepository routeReadRepository,
	    ParcelReadRepository parcelReadRepository) {
	final RouteModelMapper routeModelMapper = Mappers.getMapper(RouteModelMapper.class);
	return new RouteRepositoryImpl(routeReadRepository, parcelReadRepository, routeModelMapper);
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
