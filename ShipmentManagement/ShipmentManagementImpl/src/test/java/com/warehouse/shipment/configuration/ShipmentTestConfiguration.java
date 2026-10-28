package com.warehouse.shipment.configuration;

import org.mockito.Mockito;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;

import com.warehouse.organisationstructure.department.application.port.primary.DepartmentPort;
import com.warehouse.shipment.application.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentInternalController;
import com.warehouse.returning.api.ReturningApiService;
import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.voronoi.VoronoiService;

@ComponentScan(basePackages = { "com.warehouse.shipment"})
@EntityScan(basePackages = { "com.warehouse.shipment"})
@EnableJpaRepositories(basePackages = { "com.warehouse.shipment"})
public class ShipmentTestConfiguration {

    @MockitoBean
    public JavaMailSender javaMailSender;

    @MockitoBean
    public ShipmentInternalController shipmentInternalController;

    @MockitoBean
    public DepartmentPort departmentPort;

    @MockitoBean
    public VoronoiService voronoiService;

    @MockitoBean
    public RouteLogServicePort routeLogServicePort;

    @Bean
    ReturningApiService externalMicroserviceFeignClient() {
        return Mockito.mock(ReturningApiService.class);
    }

    @Bean
    ReturnProperties returnProperties() {
        final ReturnProperties returnProperties = new ReturnProperties();
        returnProperties.setUrl("http://localhost:8070");
        returnProperties.setEndpoint("/v1/api/returns");
        return returnProperties;
    }

}
