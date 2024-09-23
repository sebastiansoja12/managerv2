package com.warehouse.zebrareturn.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebrareturn.domain.port.primary.ZebraReturnPort;
import com.warehouse.zebrareturn.domain.port.primary.ZebraReturnPortImpl;
import com.warehouse.zebrareturn.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebrareturn.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.ReturnServiceAdapter;

@Configuration
public class ZebraConfiguration {

    @Bean
    public ZebraReturnPort zebraPort(ReturnServicePort returnServicePort, RouteLogServicePort routeLogServicePort) {
        return new ZebraReturnPortImpl(returnServicePort, routeLogServicePort);
    }

    @Bean("zebra.returnProperties")
    public ReturnProperties returnProperties() {
        return new ReturnProperties();
    }

    @Bean("zebra.routeTrackerLogProperties")
    public RouteTrackerLogProperties routeTrackerLogProperties() {
        return new RouteTrackerLogProperties();
    }

    @Bean
    public ReturnServicePort returnServicePort(ReturnProperties returnProperties) {
        return new ReturnServiceAdapter(returnProperties);
    }
}
