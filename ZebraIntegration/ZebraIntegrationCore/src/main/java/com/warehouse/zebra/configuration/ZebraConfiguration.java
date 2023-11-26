package com.warehouse.zebra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.zebra.domain.port.primary.ZebraPort;
import com.warehouse.zebra.domain.port.primary.ZebraPortImpl;
import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.infrastructure.adapter.primary.ZebraAdapter;
import com.warehouse.zebra.infrastructure.adapter.secondary.ReturnServiceAdapter;
import com.warehouse.zebra.infrastructure.adapter.secondary.properties.ReturnProperty;

@Configuration
public class ZebraConfiguration {

    @Bean
    public ZebraPort zebraPort(ReturnServicePort returnServicePort) {
        return new ZebraPortImpl(returnServicePort);
    }

    @Bean
    public ReturnServicePort returnServicePort(ReturnProperty returnProperty) {
        return new ReturnServiceAdapter(returnProperty);
    }

    @Bean
    public ReturnProperty returnProperty() {
        return new ReturnProperty();
    }

    @Bean
    public ZebraAdapter zebraAdapter(ZebraPort zebraPort) {
        return new ZebraAdapter(zebraPort);
    }
}
