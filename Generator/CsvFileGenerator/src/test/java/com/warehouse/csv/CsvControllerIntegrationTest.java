package com.warehouse.csv;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CsvControllerIntegrationTest.CsvControllerTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DatabaseSetup("/dataset/shipment.xml")
public class CsvControllerIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.csv" })
    @EntityScan(basePackages = { "com.warehouse.csv" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.csv" })
    @EnableAutoConfiguration
    public static class CsvControllerTestConfiguration {
        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8080").build();
        }
    }

    @Autowired
    private RestClient restClient;

    @Test
    void shouldGenerateCsv() {
        // given
        final Long parcelId = 1L;
        // when
        final ResponseEntity<Void> csv = restClient
                .get()
                .uri("/v2/api/csv/{id}", parcelId)
                .retrieve()
                .toBodilessEntity();
        // then
        assertThat(csv.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }
}
