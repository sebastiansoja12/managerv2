package com.warehouse.routetracker;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.routetracker.infrastructure.api.dto.RouteProcessDto;

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
    void shouldCreateProcess() throws Exception {
        // given
        final String requestPath = "src/test/resources/routetrackercontrollerintegrationtest/request.json";
        final String responsePath = "src/test/resources/routetrackercontrollerintegrationtest/response.json";
        final String request = readFileAsString(requestPath);
        final String response = readFileAsString(responsePath);
        // when
        final ResponseEntity<RouteProcessDto> responseEntity = restClient
                .post()
                .uri("/v2/api/routes/test/initialize")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(RouteProcessDto.class);
        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertThat(responseEntity)
                .extracting(ResponseEntity::getBody)
                .isEqualTo(response);
    }

    @Test
    void shouldFindProcess() {
        // given
        final Long parcelId = 1L;
        // when
        final ResponseEntity<RouteLogRecordToChange> responseEntity = restClient
                .get()
                .uri("/v2/api/routes/test/{parcelId}", parcelId)
                .retrieve()
                .toEntity(RouteLogRecordToChange.class);
        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
