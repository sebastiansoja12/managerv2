package com.warehouse.zebra;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;

@ComponentScan(basePackages = { "com.warehouse.zebra", "com.warehouse.returning", "com.warehouse.exceptionhandler",
		"com.warehouse.tools.returning", "com.warehouse.routetracker" })
@EntityScan(basePackages = { "com.warehouse.zebra", "com.warehouse.returning", "com.warehouse.routetracker" })
@EnableJpaRepositories(basePackages = { "com.warehouse.zebra", "com.warehouse.returning", "com.warehouse.routetracker" })
@EnableAutoConfiguration
public class ZebraTestConfiguration {

    @MockBean
    public ReturnServicePort returnServicePort;

    @MockBean
    public ReturnProperties returnProperties;

    @MockBean
    public RouteLogServicePort routeLogServicePort;

    @LocalServerPort
    private Integer port;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:" + port).build();
    }
}
