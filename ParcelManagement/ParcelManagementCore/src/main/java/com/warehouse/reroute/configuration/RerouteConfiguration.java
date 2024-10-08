package com.warehouse.reroute.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.domain.port.secondary.LoggerFactory;
import com.warehouse.reroute.domain.port.secondary.MailServicePort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.service.*;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.LoggerFactoryImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenRepositoryImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenMapper;

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
			RerouteTokenRepository rerouteTokenRepository) {
		final com.warehouse.reroute.domain.service.RerouteService rerouteService = new RerouteServiceImpl(
				mailServicePort, rerouteTokenRepository);
		final RerouteTokenGeneratorService rerouteTokenGeneratorService = new RerouteTokenGeneratorServiceImpl();
		return new RerouteTokenPortImpl(rerouteService, rerouteTokenGeneratorService,
				LOGGER_FACTORY.getLogger(RerouteTokenPort.class));
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
