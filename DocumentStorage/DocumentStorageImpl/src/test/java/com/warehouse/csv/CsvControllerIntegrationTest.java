package com.warehouse.csv;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CsvControllerIntegrationTest.CsvControllerTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DatabaseSetup("/dataset/shipment.xml")
public class CsvControllerIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.csv" })
    @EntityScan(basePackages = { "com.warehouse.csv" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.csv" })
    @EnableAutoConfiguration
    public static class CsvControllerTestConfiguration {
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateCsv() throws Exception {
        // given
        final Long parcelId = 1L;
        // when
        mockMvc.perform(get("/v2/api/csv/{id}", parcelId).contextPath("/v2/api"))
        // then
                .andExpect(status().isOk());
    }
}
