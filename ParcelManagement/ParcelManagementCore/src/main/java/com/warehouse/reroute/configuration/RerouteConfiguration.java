package com.warehouse.reroute.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.domain.port.secondary.ParcelPort;
import com.warehouse.reroute.domain.port.secondary.ParcelRepository;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenServicePort;
import com.warehouse.reroute.domain.service.*;
import com.warehouse.reroute.infrastructure.adapter.secondary.*;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RequestMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ResponseMapper;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;

@Configuration
public class RerouteConfiguration {

	@Bean
	public RerouteTokenRepository rerouteTokenRepository(RerouteTokenReadRepository repository) {
		final RerouteTokenMapper rerouteTokenMapper = Mappers.getMapper(RerouteTokenMapper.class);
		return new RerouteTokenRepositoryImpl(rerouteTokenMapper, repository);
	}

	@Bean
	public RerouteTokenAdapter rerouteTokenAdapter(RerouteTokenRepository rerouteTokenRepository,
			MailService mailService) {
		final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);
		final ResponseMapper responseMapper = Mappers.getMapper(ResponseMapper.class);
		return new RerouteTokenAdapter(requestMapper, rerouteTokenRepository, mailService, responseMapper);
	}

	@Bean
	public RerouteTokenPort rerouteTokenPort(RerouteTokenServicePort rerouteTokenServicePort, ParcelPort parcelPort,
			RerouteTokenValidatorService rerouteTokenValidatorService, RerouteTokenRepository rerouteTokenRepository) {
		final com.warehouse.reroute.domain.service.RerouteService rerouteService = new RerouteServiceImpl(
				rerouteTokenServicePort, parcelPort, rerouteTokenRepository);
		return new RerouteTokenPortImpl(rerouteService, rerouteTokenValidatorService);
	}

	@Bean
	public RerouteTokenServicePort rerouteTokenServicePort(RerouteTokenRepository rerouteTokenRepository,
			MailService mailService) {
		return new RerouteTokenAdapter(Mappers.getMapper(RequestMapper.class), rerouteTokenRepository, mailService,
				Mappers.getMapper(ResponseMapper.class));
	}

	@Bean
	public RerouteTokenValidatorService rerouteTokenValidatorService(RerouteTokenReadRepository repository) {
		return new RerouteTokenValidatorServiceImpl(repository);
	}

	@Bean(name = "reroute.parcelRepository")
	public ParcelRepository parcelRepository(ParcelShipmentReadRepository repository) {
		final ParcelMapper parcelMapper = Mappers.getMapper(ParcelMapper.class);
		return new ParcelRepositoryImpl(parcelMapper, repository);
	}

	@Bean
	public ShipmentService shipmentService(ShipmentPort shipmentPort) {
		final ShipmentRequestMapper requestMapper = Mappers.getMapper(ShipmentRequestMapper.class);
		final ShipmentResponseMapper responseMapper = Mappers.getMapper(ShipmentResponseMapper.class);
		return new ShipmentServiceAdapter(requestMapper, responseMapper, shipmentPort);
	}

	@Bean(name = "reroute.parcelPort")
	public ParcelPort parcelPort(ShipmentService shipmentService) {
		return new ParcelAdapter(shipmentService, Mappers.getMapper(ParcelMapper.class));
	}

	@Bean
	public RerouteService rerouteService(RerouteTokenServicePort rerouteTokenServicePort, ParcelPort parcelPort,
			RerouteTokenRepository rerouteTokenRepository) {
        return new RerouteServiceImpl(rerouteTokenServicePort, parcelPort, rerouteTokenRepository);
    }

	@Bean
	public ExpiredRerouteTokenJobDeleter expiredRerouteTokenJobDeleter(RerouteTokenReadRepository repository) {
		return new ExpiredRerouteTokenJobDeleterImpl(repository);
	}

}
