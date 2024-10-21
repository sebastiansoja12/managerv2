package com.warehouse.zebrareturn.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebrareturn.domain.port.primary.ZebraReturnPort;
import com.warehouse.zebrareturn.domain.port.primary.ZebraReturnPortImpl;
import com.warehouse.zebrareturn.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.ReturnServiceAdapter;

@Configuration
public class ZebraReturnConfiguration {

    @Bean("zebraReturn.zebraPort")
    public ZebraReturnPort zebraPort(ReturnServicePort returnServicePort) {
        return new ZebraReturnPortImpl(returnServicePort);
    }

    @Bean("zebraReturn.returnProperties")
    public ReturnProperties returnProperties() {
        return new ReturnProperties();
    }

    @Bean("zebraReturn.routeTrackerLogProperties")
    public RouteTrackerLogProperties routeTrackerLogProperties() {
        return new RouteTrackerLogProperties();
    }

    @Bean("zebraReturn.returnServicePort")
    public ReturnServicePort returnServicePort(ReturnProperties returnProperties) {
        return new ReturnServiceAdapter(returnProperties);
    }
}
