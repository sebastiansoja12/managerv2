package com.warehouse.deliveryreturn.configuration;


import com.warehouse.deliveryreturn.domain.port.secondary.*;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.MailProperty;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelProperty;
import com.warehouse.mail.domain.port.primary.MailPort;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPort;
import com.warehouse.deliveryreturn.domain.port.primary.DeliveryReturnPortImpl;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.*;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelStatusProperty;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.RouteLogProperty;

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
    public MailServicePort mailServicePort( MailPort mailPort, MailProperty mailProperty) {
        return new MailAdapter(mailPort, mailProperty);
    }

    @Bean
    public ParcelRepositoryServicePort parcelRepositoryServicePort(@NotNull ParcelProperty parcelProperty) {
        return new ParcelRepositoryServiceAdapter(parcelProperty);
    }

    @Bean
    public RouteLogProperty routeLogProperty() {
        return new RouteLogProperty();
    }

    @Bean
    public RouteLogServicePort logServicePort(RouteLogProperty routeLogProperty) {
        return new RouteLogServiceAdapter(routeLogProperty);
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

    @Bean("deliveryReturn.parcelStatusControlChangeServicePort")
    public ParcelStatusControlChangeServicePort parcelStatusControlChangeServicePort(
			ParcelStatusProperty parcelStatusProperty) {
        return new ParcelStatusControlChangeServiceAdapter(parcelStatusProperty);
    }

    @Bean
    public ParcelStatusProperty parcelStatusProperty() {
        return new ParcelStatusProperty();
    }

}
