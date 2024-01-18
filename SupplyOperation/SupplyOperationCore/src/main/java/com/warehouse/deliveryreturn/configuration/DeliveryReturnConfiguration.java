package com.warehouse.deliveryreturn.configuration;


import com.warehouse.tools.mail.MailProperty;
import com.warehouse.tools.returntoken.ReturnTokenProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import com.warehouse.deliveryreturn.domain.port.secondary.*;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.*;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.tools.shipment.ShipmentProperties;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class DeliveryReturnConfiguration {

	@Bean
	public DeliveryReturnPort deliveryReturnPort(DeliveryReturnRepository deliveryReturnRepository,
			DeliveryReturnTokenServicePort deliveryReturnTokenServicePort, RouteLogServicePort routeLogServicePort,
			ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort,
			ParcelRepositoryServicePort parcelRepositoryServicePort, MailServicePort mailServicePort) {
		final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
				deliveryReturnTokenServicePort, parcelRepositoryServicePort, mailServicePort);
		return new DeliveryReturnPortImpl(deliveryReturnService, parcelStatusControlChangeServicePort,
                routeLogServicePort);
	}

    @Bean("deliveryReturn.mailServicePort")
    public MailServicePort mailServicePort(MailPort mailPort, MailProperty mailProperty) {
        return new MailAdapter(mailPort, mailProperty);
    }

    @Bean(name = "deliveryreturn.shipmentProperties")
    public ShipmentProperties shipmentProperties() {
        return new ShipmentProperties();
    }

    @Bean
    public ParcelRepositoryServicePort parcelRepositoryServicePort(@NotNull ShipmentProperties shipmentProperties) {
        return new ParcelRepositoryServiceAdapter(shipmentProperties);
    }

    @Bean
    public RouteLogServicePort logServicePort(RouteTrackerLogProperties routeTrackerLogProperties) {
        return new RouteLogServiceAdapter(routeTrackerLogProperties);
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
    public DeliveryReturnTokenServicePort deliveryReturnTokenServicePort(ReturnTokenProperties returnTokenProperties) {
        return DeliveryReturnServiceAdapter
                .builder()
                .returnTokenProperties(returnTokenProperties)
                .restClient(RestClient.builder().baseUrl(returnTokenProperties().getUrl()).build())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "service.mock", havingValue = "true")
    public DeliveryReturnTokenServicePort deliveryReturnTokenMockServicePort() {
        return new DeliveryReturnServiceMockAdapter();
    }

    @Bean("deliveryReturn.parcelStatusProperties")
    public ParcelStatusProperties parcelStatusProperties() {
        return new ParcelStatusProperties();
    }

    @Bean("deliveryReturn.parcelStatusControlChangeServicePort")
    public ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort(
			ParcelStatusProperties parcelStatusProperties) {
        return new ParcelStatusControlChangeServiceAdapter(parcelStatusProperties);
    }
}
