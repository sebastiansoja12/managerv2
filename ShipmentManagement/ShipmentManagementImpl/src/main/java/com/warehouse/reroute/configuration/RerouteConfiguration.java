package com.warehouse.reroute.configuration;

import com.warehouse.reroute.domain.port.secondary.*;
import com.warehouse.reroute.infrastructure.adapter.secondary.*;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;
import io.github.resilience4j.retry.RetryConfig;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.domain.service.*;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenMapper;

import java.time.Duration;

@Configuration
public class RerouteConfiguration {

	private final LoggerFactory LOGGER_FACTORY = new LoggerFactoryImpl();

	@Bean
	public RerouteTokenRepository rerouteTokenRepository(RerouteTokenReadRepository repository) {
		final RerouteTokenMapper rerouteTokenMapper = Mappers.getMapper(RerouteTokenMapper.class);
		return new RerouteTokenRepositoryImpl(rerouteTokenMapper, repository);
	}

	@Bean
	public RerouteTokenPort rerouteTokenPort(MailServicePort mailServicePort,
			RerouteTokenRepository rerouteTokenRepository, final RerouteTrackerServicePort rerouteTrackerServicePort,
											 final SoftwareConfigurationServicePort softwareConfigurationServicePort) {
		final com.warehouse.reroute.domain.service.RerouteService rerouteService = new RerouteServiceImpl(
				mailServicePort, rerouteTokenRepository);
		final RerouteTokenGeneratorService rerouteTokenGeneratorService = new RerouteTokenGeneratorServiceImpl();
		return new RerouteTokenPortImpl(rerouteService, rerouteTokenGeneratorService,
				LOGGER_FACTORY.getLogger(RerouteTokenPort.class), rerouteTrackerServicePort, softwareConfigurationServicePort);
	}

	@Bean
	public SoftwareConfigurationServicePort softwareConfigurationServicePort() {
		final RetryConfig config = RetryConfig.custom()
				.maxAttempts(4)
				.waitDuration(Duration.ofSeconds(2))
				.retryExceptions(RuntimeException.class)
				.writableStackTraceEnabled(true)
				.build();
		return new SoftwareConfigurationServiceAdapter(config, softwareConfigurationProperties());
	}

	@Bean("reroute.softwareConfigurationProperties")
	public SoftwareConfigurationProperties softwareConfigurationProperties() {
		return new SoftwareConfigurationProperties();
	}

	@Bean
	public RerouteTrackerServicePort rerouteTrackerServicePort() {
		final RetryConfig config = RetryConfig.custom()
				.maxAttempts(4)
				.waitDuration(Duration.ofSeconds(2))
				.retryExceptions(RuntimeException.class)
				.writableStackTraceEnabled(true)
				.build();
		return new RerouteTrackerServiceAdapter(config);
	}

	@Bean("reroute.routeTrackerLogProperties")
	public RouteTrackerLogProperties routeTrackerLogProperties() {
		return new RouteTrackerLogProperties();
	}

	@Bean(name = "reroute.mailServicePort")
	public MailServicePort mailServicePort(MailPort mailPort) {
		return new MailAdapter(mailPort, Mappers.getMapper(NotificationMapper.class));
	}

	@Bean
	public RerouteTokenValidatorService rerouteTokenValidatorService(RerouteTokenReadRepository repository) {
		return new RerouteTokenValidatorServiceImpl(repository);
	}

	@Bean
	public RerouteService rerouteService(MailServicePort mailServicePort,
			RerouteTokenRepository rerouteTokenRepository) {
		return new RerouteServiceImpl(mailServicePort, rerouteTokenRepository);
	}

	@Bean
	public ExpiredRerouteTokenJobDeleter expiredRerouteTokenJobDeleter(RerouteTokenReadRepository repository) {
		return new ExpiredRerouteTokenJobDeleter(repository);
	}

	@Bean
	public RerouteTokenRequestMapper requestMapper() {
		return new RerouteTokenRequestMapperImpl();
	}

	@Bean
	public RerouteTokenResponseMapper responseMapper() {
		return new RerouteTokenResponseMapperImpl();
	}

}
