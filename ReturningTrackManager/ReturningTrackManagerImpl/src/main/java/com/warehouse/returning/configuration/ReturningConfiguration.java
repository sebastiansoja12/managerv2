package com.warehouse.returning.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.application.port.primary.ReturnPortImpl;
import com.warehouse.returning.application.service.ReturnProcessingService;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.event.infrastructure.adapter.secondary.SpringDomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.port.secondary.ShipmentNotifyClientPort;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnServiceImpl;
import com.warehouse.returning.domain.service.ReturnTokenGeneratorServiceImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningRepositoryImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.ShipmentNotifyClientAdapter;
import com.warehouse.returning.infrastructure.adapter.secondary.ShipmentNotifyClientMockAdapter;


@Configuration
public class ReturningConfiguration {

    @Bean
    public ReturnPort returnPort(final ReturnService returnService,
                                 final ReturnProcessingService returnProcessingService) {
        return new ReturnPortImpl(returnService, new ReturnTokenGeneratorServiceImpl(), returnProcessingService);
    }

    @Bean
    public DomainEventPublisher domainEventPublisher(final ApplicationEventPublisher eventPublisher) {
        return new SpringDomainEventPublisher(eventPublisher);
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
