package com.warehouse.deliveryreturn;


import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.configuration.DeliveryReturnTestConfiguration;

// TODO
@SpringBootTest(classes = DeliveryReturnTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Disabled
public class DeliveryReturnIntegrationTest {

    @Autowired
    private RestClient restClient;

    private final String REQUEST_PATH = "src/test/resources/deliveryreturn/integration/request.xml";
    private final String RESPONSE_PATH = "src/test/resources/deliveryreturn/integration/response.xml";

    @Test
    void shouldDeliverReturn() throws Exception {
        // given
        final String request = readFileAsString(REQUEST_PATH);
        // when
        final ResponseEntity<String> deliverReturn = restClient
                .post()
                .uri("/v2/api/delivery-returns")
                .body(request)
                .contentType(MediaType.APPLICATION_XML)
                .retrieve()
                .onStatus(HttpStatusCode::is3xxRedirection, (req, res) -> {})
                .toEntity(String.class);
        // then
        assertThat(deliverReturn.getBody()).isEqualTo("abc");
    }

    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
