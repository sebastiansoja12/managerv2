package com.warehouse.deliveryreturn;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.deliveryreturn.configuration.DeliveryReturnTestConfiguration;

@SpringBootTest(classes = DeliveryReturnTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class DeliveryReturnIntegrationTest {

    @Autowired
    private RestClient restClient;


    @Test
    void shouldDeliverReturn() {
        // given

        // when

        // then
    }
}
