package com.warehouse.route.configuration;

import com.warehouse.route.infrastructure.adapter.secondary.RouteDepotReadRepository;
import com.warehouse.route.infrastructure.adapter.secondary.RouteLogEventPublisherImpl;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.port.secondary.RouteLogServiceImpl;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapper;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapperImpl;
import com.warehouse.route.infrastructure.adapter.secondary.RouteReadRepository;
import com.warehouse.route.infrastructure.adapter.secondary.RouteRepositoryImpl;
import com.warehouse.route.infrastructure.adapter.secondary.mapper.RouteModelMapper;

@Configuration
public class  RouteConfiguration {

	@Bean
	public RouteRepository routeRepository(RouteReadRepository routeReadRepository,
			RouteDepotReadRepository routeDepotReadRepository) {
		final RouteModelMapper routeModelMapper = Mappers.getMapper(RouteModelMapper.class);
		return new RouteRepositoryImpl(routeReadRepository, routeDepotReadRepository, routeModelMapper);
	}

    @Bean
    public RouteLogEventPublisher routeLogEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new RouteLogEventPublisherImpl(applicationEventPublisher);
    }

    @Bean
    public RouteTrackerLogPort routeTrackerLogPort(RouteLogService routeLogService) {
        return new RouteTrackerLogPortImpl(routeLogService);
    }

    @Bean
    public RouteLogService routeLogService(RouteRepository routeRepository) {
        return new RouteLogServiceImpl(routeRepository);
    }

    @Bean
    public EventMapper eventMapper() {
        return new EventMapperImpl();
    }

}
