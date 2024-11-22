package com.warehouse.deliveryreturn.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.primary.*;
import com.warehouse.deliveryreturn.domain.port.secondary.*;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnService;
import com.warehouse.deliveryreturn.domain.service.DeliveryReturnServiceImpl;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.*;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.tools.mail.MailProperty;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;
import com.warehouse.tools.returntoken.ReturnTokenProperties;
import com.warehouse.tools.shipment.ShipmentProperties;
import com.warehouse.tools.supplier.SupplierValidatorProperties;

import jakarta.validation.constraints.NotNull;

@Configuration
public class DeliveryReturnConfiguration {

	@Bean
	public DeliveryReturnPort deliveryReturnPort(final DeliveryReturnRepository deliveryReturnRepository,
			final DeliveryReturnTokenServicePort deliveryReturnTokenServicePort,
			final ShipmentStatusControlServicePort shipmentStatusControlServicePort,
			final ParcelRepositoryServicePort parcelRepositoryServicePort, MailServicePort mailServicePort,
			final RouteLogReturnServicePort routeLogReturnServicePort) {
		final DeliveryReturnService deliveryReturnService = new DeliveryReturnServiceImpl(deliveryReturnRepository,
				deliveryReturnTokenServicePort, parcelRepositoryServicePort, mailServicePort);
		return new DeliveryReturnPortImpl(deliveryReturnService, shipmentStatusControlServicePort,
				routeLogReturnServicePort);
	}
    
    @Bean
    public RouteLogReturnServicePort routeLogReturnServicePort(final RouteLogEventPublisher routeLogEventPublisher) {
        return new RouteLogReturnServiceAdapter(routeLogEventPublisher);
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
    public ShipmentStatusControlServicePort parcelStatusControlChangeServicePort(
			ParcelStatusProperties parcelStatusProperties) {
        return new ShipmentStatusControlServiceAdapter(parcelStatusProperties);
    }
    
    @Bean
	public SupplierCodeValidatorPort supplierCodeValidatorPort(
			SupplierCodeValidatorServicePort supplierCodeValidatorServicePort) {
        return new SupplierCodeValidatorPortImpl(supplierCodeValidatorServicePort);
    }
    

    @Bean
	public SupplierCodeValidatorServicePort supplierCodeValidatorServicePort(
			SupplierValidatorProperties supplierValidatorProperties) {
		return SupplierCodeValidatorServiceAdapter
                .builder()
                .supplierValidatorProperties(supplierValidatorProperties)
				.restClient(RestClient
                        .builder()
                        .baseUrl(supplierValidatorProperties.getUrl())
                        .build())   
                .build();
    }
}
