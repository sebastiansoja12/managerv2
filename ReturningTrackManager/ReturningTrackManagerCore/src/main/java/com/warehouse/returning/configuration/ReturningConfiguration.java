package com.warehouse.returning.configuration;


import com.warehouse.returning.domain.port.secondary.RouteLogServicePort;
import com.warehouse.returning.infrastructure.adapter.secondary.RouteLogServiceAdapter;
import com.warehouse.returning.infrastructure.adapter.secondary.properties.RouteTrackerLogProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.port.primary.ReturnPortImpl;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnServiceImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningRepositoryImpl;

@Configuration
public class ReturningConfiguration {

    @Bean
    public ReturnPort returnPort(ReturnRepository returnRepository, RouteLogServicePort routeLogServicePort) {
        final ReturnService returnService = new ReturnServiceImpl(returnRepository);
        return new ReturnPortImpl(returnService, routeLogServicePort);
    }

	@Bean("returning.routeLogServicePort")
	public RouteLogServicePort routeLogServicePort(
			@Qualifier("returning.routeLogProperties") RouteTrackerLogProperties routeTrackerLogProperties) {
		return new RouteLogServiceAdapter(routeTrackerLogProperties);
	}

    @Bean
	public ReturnRepository returnRepository(ReturnReadRepository repository) {
        return new ReturningRepositoryImpl(repository);
    }
}
