package com.warehouse.deliveryreturn.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import com.warehouse.deliveryreturn.domain.port.secondary.*;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.*;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.tools.parcelstatus.ShipmentStatusProperties;
import com.warehouse.tools.returntoken.ReturnTokenProperties;
import com.warehouse.tools.shipment.ShipmentProperties;

import jakarta.validation.constraints.NotNull;

@Configuration
public class DeliveryReturnConfiguration {

	@Bean
	public DeliveryReturnPort deliveryReturnPort(final DeliveryReturnRepository deliveryReturnRepository,
                                                 final ReturnTokenServicePort returnTokenServicePort,
                                                 final ShipmentStatusControlServicePort shipmentStatusControlServicePort,
                                                 final ShipmentRepositoryServicePort shipmentRepositoryServicePort, MailServicePort mailServicePort,
                                                 final RouteLogReturnServicePort routeLogReturnServicePort) {
		final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
                returnTokenServicePort, shipmentRepositoryServicePort, mailServicePort);
		return new DeliveryReturnPortImpl(deliveryReturnService, shipmentStatusControlServicePort,
				routeLogReturnServicePort);
	}
    
    @Bean
    public RouteLogReturnServicePort routeLogReturnServicePort(final RouteLogEventPublisher routeLogEventPublisher) {
        return new RouteLogReturnServiceAdapter(routeLogEventPublisher);
    }

    @Bean("deliveryReturn.mailServicePort")
    public MailServicePort mailServicePort(final NotificationEventPublisher eventPublisher) {
        return new MailAdapter(eventPublisher);
    }

    @Bean(name = "deliveryreturn.shipmentProperties")
    public ShipmentProperties shipmentProperties() {
        return new ShipmentProperties();
    }

    @Bean
    public ShipmentRepositoryServicePort parcelRepositoryServicePort(@NotNull ShipmentProperties shipmentProperties) {
        return new ShipmentRepositoryServiceAdapter(shipmentProperties);
    }

    @Bean
    public DeliveryReturnRepository deliveryReturnRepository(DeliveryReturnReadRepository deliveryReturnReadRepository) {
        return new DeliveryReturnRepositoryImpl(deliveryReturnReadRepository);
    }

    @Bean("deliveryReturn.returnTokenProperties")
    @Primary
    public ReturnTokenProperties returnTokenProperties() {
        return new ReturnTokenProperties();
    }

    @Bean
    @ConditionalOnProperty(name = "service.mock", havingValue = "false")
    public ReturnTokenServicePort deliveryReturnTokenServicePort(ReturnTokenProperties returnTokenProperties) {
        return ReturnTokenServiceAdapter
                .builder()
                .returnTokenProperties(returnTokenProperties)
                .restClient(RestClient.builder().baseUrl(returnTokenProperties().getUrl()).build())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "service.mock", havingValue = "true")
    public ReturnTokenServicePort deliveryReturnTokenMockServicePort() {
        return new ReturnTokenServiceMockAdapter();
    }

    @Bean("deliveryReturn.parcelStatusProperties")
    public ShipmentStatusProperties parcelStatusProperties() {
        return new ShipmentStatusProperties();
    }

    @Bean("deliveryReturn.parcelStatusControlChangeServicePort")
    public ShipmentStatusControlServicePort parcelStatusControlChangeServicePort(
			final ShipmentProperties shipmentProperties) {
        return new ShipmentStatusControlServiceAdapter(shipmentProperties);
    }
}
