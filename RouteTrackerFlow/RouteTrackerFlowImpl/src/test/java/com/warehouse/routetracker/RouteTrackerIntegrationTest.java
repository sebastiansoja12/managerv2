package com.warehouse.routetracker;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;

// TODO to be fixed

@SpringBootTest(classes = RouteTrackerIntegrationTest.RouteTrackerIntegrationTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Disabled
public class RouteTrackerIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.routetracker" })
    @EntityScan(basePackages = { "com.warehouse.routetracker" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.routetracker" })
    public static class RouteTrackerIntegrationTestConfiguration {
        
        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8080").build();
        }
    }

    @Autowired
    private RestClient restClient;

    @Test
    void shouldFindProcess() {
        // given
        final Long parcelId = 1L;
        // when
        final ResponseEntity<RouteLogRecord> responseEntity = restClient
                .get()
                .uri("/v2/api/routes/{parcelId}", parcelId)
                .retrieve()
                .toEntity(RouteLogRecord.class);
        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

}
