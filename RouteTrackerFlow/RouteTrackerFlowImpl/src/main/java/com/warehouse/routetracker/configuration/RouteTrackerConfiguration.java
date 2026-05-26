package com.warehouse.routetracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPortImpl;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteLogRecordReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteLogRepositoryImpl;

@Configuration
@EnableKafka
public class RouteTrackerConfiguration {

	@Bean
	public RouteLogRepository routeRepository(RouteLogRecordReadRepository routeLogRecordReadRepository) {
		return new RouteLogRepositoryImpl(routeLogRecordReadRepository);
	}

	@Bean
	public RouteTrackerLogPort routeTrackerLogPort(RouteLogRepository repository) {
		return new RouteTrackerLogPortImpl(repository);
	}
}
