package com.warehouse.reroute.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.domain.port.secondary.*;
import com.warehouse.reroute.domain.service.*;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenRequestMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.RerouteTokenResponseMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.*;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenMapper;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.voronoi.VoronoiService;

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
			RerouteTokenRepository rerouteTokenRepository, ParcelReroutePort parcelReroutePort) {
		final com.warehouse.reroute.domain.service.RerouteService rerouteService = new RerouteServiceImpl(
				mailServicePort, rerouteTokenRepository);
		final RerouteTokenGeneratorService rerouteTokenGeneratorService = new RerouteTokenGeneratorServiceImpl();
		return new RerouteTokenPortImpl(rerouteService, parcelReroutePort, rerouteTokenGeneratorService,
				LOGGER_FACTORY.getLogger(RerouteTokenPort.class));
	}

	@Bean(name = "reroute.mailServicePort")
	public MailServicePort mailServicePort(MailService mailService) {
		return new MailAdapter(mailService, Mappers.getMapper(NotificationMapper.class));
	}

	@Bean
	public RerouteTokenValidatorService rerouteTokenValidatorService(RerouteTokenReadRepository repository) {
		return new RerouteTokenValidatorServiceImpl(repository);
	}

	@Bean
	public ShipmentService shipmentService(ShipmentReroutePort shipmentReroutePort) {
		final ShipmentRequestMapper requestMapper = Mappers.getMapper(ShipmentRequestMapper.class);
		final ShipmentResponseMapper responseMapper = Mappers.getMapper(ShipmentResponseMapper.class);
		return new ShipmentServiceAdapter(requestMapper, responseMapper, shipmentReroutePort);
	}

	@Bean
	public RerouteService rerouteService(MailServicePort mailServicePort,
										 RerouteTokenRepository rerouteTokenRepository) {
		return new RerouteServiceImpl(mailServicePort, rerouteTokenRepository);
	}

	@Bean
	public PathFinderServicePort pathFinderServicePort(VoronoiService voronoiService) {
		return new PathFinderAdapter(voronoiService);
	}

	@Bean
	public ExpiredRerouteTokenJobDeleter expiredRerouteTokenJobDeleter(RerouteTokenReadRepository repository) {
		return new ExpiredRerouteTokenJobDeleter(repository);
	}

	@Bean
	public ParcelReroutePort parcelReroutePort(ShipmentService shipmentService) {
		return new ParcelRerouteAdapter(shipmentService, Mappers.getMapper(ParcelMapper.class));
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
