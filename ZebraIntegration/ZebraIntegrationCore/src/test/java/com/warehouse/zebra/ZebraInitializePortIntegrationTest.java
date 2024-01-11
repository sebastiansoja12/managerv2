package com.warehouse.zebra;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(classes = ZebraTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ZebraInitializePortIntegrationTest {

    @Autowired
    private RestClient restClient;

    @Test
    void shouldProcessRequest() throws Exception {
        // given
        final String requestFileName = "src/test/resources/zebrainitializeintegration/request.xml";
        final String request = readFileAsString(requestFileName);
        final String responseFileName = "src/test/resources/zebrainitializeintegration/response.xml";
        final String response = readFileAsString(responseFileName);
        // when
        final ResponseEntity<String> entity = restClient
                .post()
                .uri("/v2/api/zebra/initialize")
                .contentType(MediaType.APPLICATION_XML)
                .body(request)
                .retrieve()
                .toEntity(String.class);
        // then
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        assertEquals(response, entity.getBody());
    }


    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
