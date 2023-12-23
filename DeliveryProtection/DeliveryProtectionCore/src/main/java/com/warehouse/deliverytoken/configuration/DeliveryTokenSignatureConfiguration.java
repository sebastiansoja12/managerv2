package com.warehouse.deliverytoken.configuration;


import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliverytoken.domain.port.primary.DeliveryTokenPort;
import com.warehouse.deliverytoken.domain.port.primary.DeliveryTokenPortImpl;
import com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.domain.service.DeliveryService;
import com.warehouse.deliverytoken.domain.service.DeliveryServiceImpl;
import com.warehouse.deliverytoken.infrastructure.adapter.primary.api.DeliveryTokenService;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.DeliveryTokenAdapter;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.DeliveryTokenMockAdapter;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.ParcelServiceAdapter;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.model.SupplierToken;
import com.warehouse.tools.shipment.ShipmentProperties;

@Configuration
public class DeliveryTokenSignatureConfiguration {


	@Bean
	public DeliveryTokenService deliveryTokenService(DeliveryTokenPort deliveryTokenPort) {
		return new com.warehouse.deliverytoken.infrastructure.adapter.primary.DeliveryTokenAdapter(deliveryTokenPort);
	}

	@Bean
	public DeliveryTokenPort deliveryTokenPort(ParcelServicePort parcelServicePort,
			DeliveryTokenServicePort deliveryTokenServicePort) {
		final DeliveryService service = new DeliveryServiceImpl(deliveryTokenServicePort);
		return new DeliveryTokenPortImpl(service, parcelServicePort);
	}

	@Bean
	@ConditionalOnProperty(name = "service.mock", havingValue = "false")
	public DeliveryTokenAdapter deliveryTokenAdapter() {
		return new DeliveryTokenAdapter();
	}

	@Bean
	@ConditionalOnProperty(name = "service.mock", havingValue = "true")
	public DeliveryTokenMockAdapter deliveryTokenMockAdapter(@Value("${allowed.supplier}") String supplier) {
		final Map<String, SupplierToken> supplierTokenMap = Map.of(supplier, new SupplierToken("tokenProtection"));
		return new DeliveryTokenMockAdapter(supplierTokenMap);
	}

	@Bean(name = "delivery.shipmentProperties")
	public ShipmentProperties shipmentProperties() {
		return new ShipmentProperties();
	}

	@Bean
	public ParcelServiceAdapter parcelServiceAdapter(ShipmentProperties shipmentProperties) {
		return new ParcelServiceAdapter(shipmentProperties);
	}
}
