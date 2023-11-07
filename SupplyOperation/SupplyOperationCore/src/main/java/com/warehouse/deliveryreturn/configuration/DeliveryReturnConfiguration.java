package com.warehouse.deliveryreturn.configuration;


import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.*;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelStatusProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import org.springframework.web.client.RestClient;

@Configuration
public class DeliveryReturnConfiguration {

	@Bean
	public DeliveryReturnPort deliveryReturnPort(DeliveryReturnRepository deliveryReturnRepository,
			DeliveryReturnTokenServicePort deliveryReturnTokenServicePort, RouteLogServicePort routeLogServicePort,
			ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort) {
		final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
				deliveryReturnTokenServicePort);
		return new DeliveryReturnPortImpl(deliveryReturnService, parcelStatusControlChangeServicePort,
                routeLogServicePort);
	}

    @Bean
    public RouteLogServicePort logServicePort(RestClient.Builder builder) {
        final RestClient restClient = builder.baseUrl("").build();
        return new RouteLogServiceAdapter(restClient);
    }

    @Bean
    public DeliveryReturnRepository deliveryReturnRepository(DeliveryReturnReadRepository deliveryReturnReadRepository) {
        return new DeliveryReturnRepositoryImpl(deliveryReturnReadRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "service.mock", havingValue = "false")
    public DeliveryReturnTokenServicePort deliveryReturnTokenServicePort() {
        return new DeliveryReturnServiceAdapter();
    }

    @Bean
    @ConditionalOnProperty(name = "service.mock", havingValue = "true")
    public DeliveryReturnTokenServicePort deliveryReturnTokenMockServicePort() {
        return new DeliveryReturnServiceMockAdapter();
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }

    @Bean
	public ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort(RestClient restClient,
			ParcelStatusProperty parcelStatusProperty) {
        return new ParcelStatusControlChangeServiceAdapter(restClient, parcelStatusProperty);
    }

}
