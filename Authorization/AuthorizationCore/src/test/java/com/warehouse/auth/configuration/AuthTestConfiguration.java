package com.warehouse.auth.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.auth.domain.provider.JwtProvider;
import com.warehouse.depot.api.DepotService;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@ComponentScan(basePackages = { "com.warehouse.auth"})
@EntityScan(basePackages = { "com.warehouse.auth"})
@EnableJpaRepositories(basePackages = { "com.warehouse.auth"})
public class AuthTestConfiguration {

    @MockBean
    public DepotService depotService;

    @MockBean
    public JwtProvider jwtProvider;

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

}
