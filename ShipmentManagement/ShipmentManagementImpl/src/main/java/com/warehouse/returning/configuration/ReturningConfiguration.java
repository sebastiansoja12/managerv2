package com.warehouse.returning.configuration;

import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnDetailsMapper;
import org.springframework.transaction.support.TransactionTemplate;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.returning.application.port.secondary.ReturnConsumedEventServicePort;
import com.warehouse.returning.application.port.primary.ReturnProcessingPort;
import com.warehouse.returning.application.port.primary.ReturnProcessingPortImpl;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.application.port.primary.ReturnPort;
import com.warehouse.returning.application.port.primary.ReturnPortImpl;
import com.warehouse.returning.application.port.secondary.ReturningTrackManagerServicePort;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnPackageRequestMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningTrackManagerServiceClientAdapter;
import com.warehouse.returning.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageOutboundMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageResponseMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ShipmentServiceMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.returning.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.tools.returning.ReturnProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ReturningConfiguration implements WebMvcConfigurer {

    @Bean
    public ReturnProcessingPort returnProcessingPort(
            final ShipmentServicePort shipmentServicePort,
            final ReturnConsumedEventServicePort consumedEventServicePort,
            final OperatorContext operatorContext,
            final TransactionTemplate transactionTemplate) {
        return new ReturnProcessingPortImpl(
                shipmentServicePort, consumedEventServicePort, operatorContext, transactionTemplate);
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, DepartmentId.class, source -> new DepartmentId(Long.valueOf(source)));
        registry.addConverter(String.class, ShipmentId.class, source -> new ShipmentId(Long.valueOf(source)));
        registry.addConverter(String.class, ReturnPackageId.class, source -> new ReturnPackageId(Long.valueOf(source)));
    }

    @Bean
    public ReturnPort returnPort(final ReturningTrackManagerServicePort returningTrackManagerServicePort,
                                 final ShipmentServicePort shipmentServicePort,
                                 final DepartmentServicePort departmentServicePort) {
        return new ReturnPortImpl(returningTrackManagerServicePort, shipmentServicePort, departmentServicePort);
    }

    @Bean
    public ShipmentServicePort shipmentServicePort(final ShipmentApiService shipmentApiService,
                                                   final ShipmentServiceMapper shipmentServiceMapper) {
        return new ShipmentServiceAdapter(shipmentApiService, shipmentServiceMapper);
    }

    @Bean
    public ShipmentServiceMapper shipmentServiceMapper() {
        return new ShipmentServiceMapper();
    }

    @Bean
    public ReturnPackageRequestMapper returnPackageRequestMapper() {
        return new ReturnPackageRequestMapper();
    }

    @Bean
    public ReturnPackageOutboundMapper returnPackageOutboundMapper() {
        return new ReturnPackageOutboundMapper();
    }

    @Bean
    public ReturnPackageResponseMapper returnPackageResponseMapper() {
        return new ReturnPackageResponseMapper();
    }

    @Bean
    public ReturningTrackManagerServicePort returningTrackManagerServicePort(
            final RestClient.Builder restClientBuilder,
            final ReturnProperties returnProperties,
            final CurrentUserApiService currentUserApiService,
            final ReturnPackageOutboundMapper returnPackageOutboundMapper,
            final ReturnPackageResponseMapper returnPackageResponseMapper,
            final DepartmentServicePort departmentServicePort) {
        return new ReturningTrackManagerServiceClientAdapter(
                restClientBuilder,
                returnProperties,
                currentUserApiService,
                returnPackageOutboundMapper,
                returnPackageResponseMapper,
                new ReturnDetailsMapper(departmentServicePort)
        );
    }
}
