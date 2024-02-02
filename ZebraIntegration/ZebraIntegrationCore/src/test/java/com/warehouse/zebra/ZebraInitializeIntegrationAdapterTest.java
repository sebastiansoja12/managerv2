package com.warehouse.zebra;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.tools.routelog.RouteTrackerLogProperties;
import com.warehouse.zebra.domain.port.secondary.RouteLogServicePort;
import com.warehouse.zebra.domain.vo.RouteProcess;
import com.warehouse.zebra.infrastructure.adapter.primary.ZebraInitializeAdapter;
import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;


@SpringBootTest(classes = ZebraTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ZebraInitializeIntegrationAdapterTest {


    @Autowired
    private RestClient restClient;

    @Autowired
    private RouteTrackerLogProperties routeTrackerLogProperties;

    @Autowired
    private ZebraInitializeAdapter zebraInitializeAdapter;

    @Autowired
    private RouteLogServicePort routeLogServicePort;


    private final String REQUEST_PATH = "src/test/resources/zebrainitializeintegration/request.xml";
    private final String WRONG_REQUEST_PATH = "src/test/resources/zebrainitializeintegration/wrong_process_type_request.xml";
    private final String RESPONSE_PATH = "src/test/resources/zebrainitializeintegration/response.xml";


    @Test
    void shouldProcessRequest() throws IOException, JAXBException {
        // given
        final Path request = Paths.get(REQUEST_PATH);
        final String requestXmlContent = new String(Files.readAllBytes(request));
        final Path response = Paths.get(RESPONSE_PATH);
        final String responseXmlContent = new String(Files.readAllBytes(response));
        when(routeLogServicePort.initializeProcess(any())).thenReturn(new RouteProcess(1L, UUID.fromString(
                "4061acb8-b556-42b1-b274-7aa300eb1112"
        )));
        // when
        final ResponseEntity<ZebraResponse> responseEntity = restClient
                .post()
                .uri("/v2/api/zebra/initialize")
                .body(requestXmlContent)
                .contentType(MediaType.APPLICATION_XML)
                .retrieve()
                .toEntity(ZebraResponse.class);
        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals(responseXmlContent, convertObjectToXmlString(responseEntity.getBody()));
    }

    @Test
    void shouldNotProcessRequestWhenProcessTypeIsWrong() throws IOException {
        // given
        final Path request = Paths.get(WRONG_REQUEST_PATH);
        final String requestXmlContent = new String(Files.readAllBytes(request));
        // when
        final Executable executable = () -> restClient
                .post()
                .uri("/v2/api/zebra/initialize")
                .body(requestXmlContent)
                .contentType(MediaType.APPLICATION_XML)
                .retrieve()
                .toEntity(ZebraResponse.class);
        // then
        assertThrows(RuntimeException.class, executable);
    }


    private String convertObjectToXmlString(ZebraResponse zebraResponse) throws JAXBException {

        final JAXBContext jaxbContext = JAXBContext.newInstance(ZebraResponse.class);

        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        final StringWriter sw = new StringWriter();

        jaxbMarshaller.marshal(zebraResponse, sw);

        return sw.toString();
    }
}
