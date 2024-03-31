package com.warehouse.routelogger.configuration;


import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPortImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteLoggerConfiguration {

    @Bean
    public RouteLoggerPort routeLoggerPort() {
        return new RouteLoggerPortImpl();
    }
}
