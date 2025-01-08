package com.warehouse.deliveryreject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPort;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPortImpl;
import com.warehouse.deliveryreject.domain.port.secondary.PersonShipmentServicePort;
import com.warehouse.deliveryreject.domain.port.secondary.RejectRepository;
import com.warehouse.deliveryreject.domain.port.secondary.RejectShipmentServicePort;
import com.warehouse.deliveryreject.domain.port.secondary.RejectTrackerServicePort;
import com.warehouse.deliveryreject.domain.service.DeliveryRejectConverterService;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.DeliveryRejectAdapter;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.*;
import com.warehouse.routelogger.RouteLogEventPublisher;

@Configuration
public class DeliveryRejectConfiguration {

    @Bean
    public DeliveryRejectPort deliveryRejectPort(final RejectService rejectService,
                                                 final RejectShipmentServicePort rejectShipmentServicePort,
                                                 final PersonShipmentServicePort personShipmentServicePort,
                                                 final DeliveryRejectConverterService deliveryRejectConverterService,
                                                 final RejectTrackerServicePort rejectTrackerServicePort) {
        return new DeliveryRejectPortImpl(rejectService, rejectShipmentServicePort, personShipmentServicePort,
                deliveryRejectConverterService, rejectTrackerServicePort);
    }

    @Bean
    public RejectTrackerServicePort rejectTrackerServicePort(final RouteLogEventPublisher routeLogEventPublisher) {
        return new RejectTrackerServiceAdapter(routeLogEventPublisher);
    }

    @Bean
    public PersonShipmentServicePort personShipmentServicePort() {
        return new PersonShipmentServiceAdapter();
    }

    @Bean
    public RejectShipmentServicePort rejectShipmentServicePort() {
        return new RejectShipmentServiceAdapter();
    }

    @Bean
    public DeliveryRejectService deliveryRejectService(final DeliveryRejectPort deliveryRejectPort) {
        return new DeliveryRejectAdapter(deliveryRejectPort);
    }

    @Bean
    public RejectRepository deliveryRejectRepository(final RejectReasonReadRepository rejectReasonReadRepository) {
        return new RejectRepositoryImpl(rejectReasonReadRepository);
    }
}
