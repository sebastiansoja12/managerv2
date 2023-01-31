package com.warehouse.auth.configuration;

import com.warehouse.depot.api.DepotService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.auth", "com.warehouse.security" })
@EntityScan(basePackages = { "com.warehouse.auth", "com.warehouse.security" })
@EnableJpaRepositories(basePackages = { "com.warehouse.auth", "com.warehouse.security" })
public class AuthTestConfiguration {

    @MockBean
    public DepotService depotService;

}
