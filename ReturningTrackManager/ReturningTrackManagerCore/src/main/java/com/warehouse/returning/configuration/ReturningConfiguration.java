package com.warehouse.returning.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.port.primary.ReturnPortImpl;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.port.secondary.ShipmentNotifyClientPort;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnServiceImpl;
import com.warehouse.returning.domain.service.ReturnTokenGeneratorServiceImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningRepositoryImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.ShipmentNotifyClientAdapter;
import com.warehouse.returning.infrastructure.adapter.secondary.ShipmentNotifyClientMockAdapter;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.tools.shipment.ShipmentProperties;

@Configuration
public class ReturningConfiguration {

    @Bean
    public ReturnPort returnPort(final ReturnService returnService) {
        return new ReturnPortImpl(returnService, new ReturnTokenGeneratorServiceImpl());
    }

    @Bean
    public ReturnService returnService(final ReturnRepository returnRepository) {
        return new ReturnServiceImpl(returnRepository);
    }

    @Bean(name = "returning.routeTrackerLogProperties")
    public RouteTrackerLogProperties routeTrackerLogProperties() {
        return new RouteTrackerLogProperties();
    }

    @Bean
	public ReturnRepository returnRepository(ReturnReadRepository repository) {
        return new ReturningRepositoryImpl(repository);
    }

    @Bean
    @ConditionalOnProperty(name = "returning.notify.mock", havingValue = "false")
    public ShipmentNotifyClientPort shipmentNotifyClientPort(final ShipmentProperties shipmentProperties) {
        return new ShipmentNotifyClientAdapter(shipmentProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "returning.notify.mock", havingValue = "true")
    public ShipmentNotifyClientPort shipmentNotifyClientMockAdapter() {
        return new ShipmentNotifyClientMockAdapter();
    }
}
