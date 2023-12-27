package com.warehouse.configuration;

import com.warehouse.tools.parcelstatus.ParcelStatusProperties;
import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.tools.shipment.ShipmentProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {

    @Bean
    public ParcelStatusProperties parcelStatusProperties() {
        return new ParcelStatusProperties();
    }

    @Bean
    public ReturnProperties returnProperties() {
        return new ReturnProperties();
    }

    @Bean
    public RouteTrackerLogProperties routeTrackerLogProperties() {
        return new RouteTrackerLogProperties();
    }

    @Bean
    public ShipmentProperties shipmentProperties() {
        return new ShipmentProperties();
    }
}
