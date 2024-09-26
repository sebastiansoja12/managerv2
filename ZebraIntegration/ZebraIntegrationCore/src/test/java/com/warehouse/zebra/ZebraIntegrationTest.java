package com.warehouse.zebra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import com.warehouse.commonassets.response.Response;
import com.warehouse.commonassets.vo.DeviceInformation;
import com.warehouse.tools.returning.ReturnProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.infrastructure.api.dto.*;
import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

@SpringBootTest(classes = ZebraTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ZebraIntegrationTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ReturnProperties returnProperties;

    @Autowired
    private ReturnServicePort returnServicePort;

    private final String REQUEST_PATH = "src/test/resources/zebraintegration/returntype/request.xml";
    private final String ERROR_REQUEST_PATH = "src/test/resources/zebraintegration/returntype/errorTypeRequest.xml";
    private final String RESPONSE_PATH = "src/test/resources/zebraintegration/returntype/response.xml";

    private final DeviceInformation deviceInformation = new DeviceInformation("1.0", 1L, "s-soja", "KT1");

    @Test
    void shouldProcessRequestWithReturnProcessType() throws IOException, JAXBException {
        // given
        final Path request = Paths.get(REQUEST_PATH);
        final Path response = Paths.get(RESPONSE_PATH);
        final String requestXmlContent = new String(Files.readAllBytes(request));
        final String responseXmlContent = new String(Files.readAllBytes(response));

		final List<com.warehouse.commonassets.vo.ProcessReturn> processReturns = List.of(
				new com.warehouse.commonassets.vo.ProcessReturn(1L, "PROCESSING"),
				new com.warehouse.commonassets.vo.ProcessReturn(2L, "PROCESSING"));

        final Response expectedResponse = new Response(deviceInformation, processReturns, Collections.emptyList());

        final ZebraResponse expectedZebraResponse = createZebraResponse();

        when(returnServicePort.processReturn(any())).thenReturn(expectedResponse);
        // when
        final ResponseEntity<ZebraResponse> zebraProcess = restClient.post()
                .uri("/v2/api/zebra")
                .body(requestXmlContent)
                .contentType(MediaType.APPLICATION_XML)
                .retrieve()
                .toEntity(ZebraResponse.class);
        // then
        assertTrue(zebraProcess.getStatusCode().isSameCodeAs(HttpStatus.OK));
        assertNotNull(zebraProcess.getBody());
        assertEquals(expectedZebraResponse.getProcessReturns(), zebraProcess.getBody().getProcessReturns());
        assertEquals(responseXmlContent, convertObjectToXmlString(zebraProcess.getBody()));
    }

    @Test
    void shouldThrowUnexpectedProcessTypeError() throws IOException {
        // given
        final Path request = Paths.get(ERROR_REQUEST_PATH);
        final String requestXmlContent = new String(Files.readAllBytes(request));
        // when
        final ResponseEntity<Void> zebraProcess = restClient.post()
                .uri("/v2/api/zebra")
                .body(requestXmlContent)
                .contentType(MediaType.APPLICATION_XML)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {})
                .toBodilessEntity();
        // then
        assertTrue(zebraProcess.getStatusCode().isSameCodeAs(HttpStatus.INTERNAL_SERVER_ERROR));
        assertNull(zebraProcess.getBody());
    }

    private String convertObjectToXmlString(ZebraResponse zebraResponse) throws JAXBException {

        final JAXBContext jaxbContext = JAXBContext.newInstance(ZebraResponse.class);

        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        final StringWriter sw = new StringWriter();

        jaxbMarshaller.marshal(zebraResponse, sw);

        return sw.toString();
    }

    private ZebraResponse createZebraResponse() {
        return new ZebraResponse(1L, "1.0", "s-soja", createProcessReturn(), null);
    }

    private List<ProcessReturn> createProcessReturn() {
		return List.of(new ProcessReturn(1L, "PROCESSING"), new ProcessReturn(2L, "PROCESSING"));
    }
}
