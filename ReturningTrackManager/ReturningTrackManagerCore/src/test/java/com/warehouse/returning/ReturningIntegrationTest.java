package com.warehouse.returning;


import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.exceptionhandler.model.ErrorResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.DeleteReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ResponseStatus;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.*;

@SpringBootTest(classes = ReturningIntegrationTest.ReturningTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReturningIntegrationTest {

    @ComponentScan(basePackages = { "com.warehouse.returning", "com.warehouse.exceptionhandler" })
    @EntityScan(basePackages = { "com.warehouse.returning" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.returning" })
    public static class ReturningTestConfiguration {

        @LocalServerPort
        private Integer port;

        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:" + port).build();
        }
    }

    @Autowired
    private RestClient restClient;

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static final String USERNAME_MISSING_JSON_PATH = "src/test/resources/returning/usernameMissing.json";
    private static final String DEPOT_CODE_MISSING_JSON_PATH = "src/test/resources/returning/depotCodeMissing.json";
    private static final String RETURN_REQUEST_JSON_PATH = "src/test/resources/returning/returnRequest.json";
    private static final String RETURN_REQUESTS_MISSING_RT_JSON_PATH = "src/test/resources/returning/returningsRequestWithMissingRT.json";
    private static final String RETURN_RESPONSES_MISSING_RT_JSON_PATH = "src/test/resources/returning/returningsResponseWithMissingRT.json";
	private static final String UPDATE_RETURN_REQUEST_JSON_PATH = "src/test/resources/returning/updateReturnRequest.json";
	private static final String WRONG_UPDATE_RETURN_REQUEST_JSON_PATH = "src/test/resources/returning/wrongUpdateReturnRequest.json";

    private static final String RETURN_MISSING_JSON_PATH = "src/test/resources/returning/returnEntityNotFound.json";


    @Test
    void shouldProcessReturning() throws Exception {
        // given
        final String request = readFileAsString(RETURN_REQUEST_JSON_PATH);
        // when
        final ResponseEntity<ReturningResponseDto> response = restClient
                .post()
                .uri("/v2/api/returns")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ReturningResponseDto.class);

        // then
        assertNotNull(response);
        assertEquals(String.format(returningResponseAsJsonString(), "PROCESSING", "PROCESSING"),
                objectMapper.writeValueAsString(response.getBody()));
    }

    @Test
    void shouldProcessReturningsWithMissingReturnTokenForOneReturn() throws Exception {
        // given
        final String request = readFileAsString(RETURN_REQUESTS_MISSING_RT_JSON_PATH);
        final String expectedResponse = readFileAsString(RETURN_RESPONSES_MISSING_RT_JSON_PATH);
        // when
        final ResponseEntity<ReturningResponseDto> response = restClient
                .post()
                .uri("/v2/api/returns")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ReturningResponseDto.class);
        // then
        assertNotNull(response);
        assertEquals(expectedResponse, objectMapper.writeValueAsString(response.getBody()));
    }

    @Test
    @DatabaseSetup("/dataset/returning.xml")
    void shouldCompleteProcessReturning() throws Exception {
        // given
        final String request = readFileAsString(UPDATE_RETURN_REQUEST_JSON_PATH);
        // when
        final ResponseEntity<ReturningResponseDto> returningResponse = restClient
                .post()
                .uri("/v2/api/returns")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ReturningResponseDto.class);

        // then
        assertNotNull(returningResponse);
        assertEquals(String.format(returningUpdateResponseAsJsonString(), "COMPLETED", "COMPLETED"),
                objectMapper.writeValueAsString(returningResponse.getBody()));
    }

    @Test
    void shouldFailWhenUserIsMissing() throws Exception {
        // given
        final ReturningRequestDto returningRequest = createReturningRequest("", "KT1", "returnToken",
                "abc");
        // when
        final ResponseEntity<ErrorResponse> returningResponse = restClient
                .post()
                .uri("/v2/api/returns")
                .body(returningRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(ErrorResponse.class);

        // then
        assertTrue(returningResponse.getStatusCode().isSameCodeAs(HttpStatus.NOT_ACCEPTABLE));
        assertNotNull(returningResponse.getBody());
        assertEquals(readFileAsString(USERNAME_MISSING_JSON_PATH),
                returningErrorResponseAsJsonString(returningResponse.getBody()));
    }

    @Test
    void shouldFailWhenDepotIsMissing() throws Exception {
        // given
        final ReturningRequestDto returningRequest = createReturningRequest("s-soja", "", "returnToken",
                "abc");
        // when
        final ResponseEntity<ErrorResponse> returningResponse = restClient
                .post()
                .uri("/v2/api/returns")
                .body(returningRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(ErrorResponse.class);

        // then
        assertTrue(returningResponse.getStatusCode().isSameCodeAs(HttpStatus.NOT_ACCEPTABLE));
        assertNotNull(returningResponse.getBody());
        assertEquals(readFileAsString(DEPOT_CODE_MISSING_JSON_PATH),
                returningErrorResponseAsJsonString(returningResponse.getBody()));
    }

    @Test
    void shouldFailWhenReturnEntityToUpdateWasNotFound() throws Exception {
        // given
        final String request = readFileAsString(WRONG_UPDATE_RETURN_REQUEST_JSON_PATH);

        // when
        final ResponseEntity<ErrorResponse> returningResponse = restClient
                .post()
                .uri("/v2/api/returns")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(ErrorResponse.class);

        // then
        assertTrue(returningResponse.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
        assertNotNull(returningResponse.getBody());
        assertEquals(readFileAsString(RETURN_MISSING_JSON_PATH),
                returningErrorResponseAsJsonString(returningResponse.getBody()));
    }

    @Test
    @DatabaseSetup("/dataset/returning.xml")
    void shouldGetReturn() {
        // given
        final Long id = 1L;
        // when
        final ReturnModelDto returningResponse = restClient
                .get()
                .uri("/v2/api/returns/{id}", id)
                .retrieve()
                .body(ReturnModelDto.class);
        // then
        assertNotNull(returningResponse);
    }

    @Test
    void shouldDeleteReturn() {
        // given
        final Long id = 1L;
        // when
        final DeleteReturnResponse deleteReturnResponse = restClient
                .delete()
                .uri("/v2/api/returns/{id}", id)
                .retrieve()
                .body(DeleteReturnResponse.class);
        // then
        assertNotNull(deleteReturnResponse);
        assertEquals(ResponseStatus.OK, deleteReturnResponse.getResponseStatus());
    }
    
    private static String returningErrorResponseAsJsonString(ErrorResponse errorResponse) {
		final String json = """
				{
				  "shipmentStatus": %s,
				  "error": "%s"
				}""";

        return String.format(json, errorResponse.getStatus(), errorResponse.getError());
    }

    private static String returningResponseAsJsonString() {
        return """
                {
                  "processReturn" : [ {
                    "shipmentId" : 2,
                    "returnId" : 6,
                    "processStatus" : "PROCESSING"
                  } ]
                }""";
    }

    private static String returningUpdateResponseAsJsonString() {
        return """
                {
                  "processReturn" : [ {
                    "shipmentId" : 3,
                    "returnId" : 4,
                    "processStatus" : "COMPLETED"
                  }, {
                    "shipmentId" : 4,
                    "returnId" : 5,
                    "processStatus" : "COMPLETED"
                  } ]
                }""";
    }

    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

	private ReturningRequestDto createReturningRequest(String username, String depotCode, String returnToken,
			String supplierCode) {
        return ReturningRequestDto.builder()
                .requests(createReturnPackageRequests(ReturnStatusDto.CREATED, returnToken, supplierCode))
                .depotCode(createDepotCode(depotCode))
                .username(createUsername(username))
                .build();
    }

    private UsernameDto createUsername(String username) {
        return UsernameDto.builder().value(username).build();
    }

    private DepotCodeDto createDepotCode(String depotCode) {
        return DepotCodeDto.builder().value(depotCode).build();
    }

    private List<ReturnPackageRequestDto> createReturnPackageRequests(ReturnStatusDto returnStatusDto,
			String returnToken, String supplierCode) {
		return List.of(createReturnPackageRequest(returnStatusDto, returnToken, supplierCode));
	}

	private ReturnPackageRequestDto createReturnPackageRequest(ReturnStatusDto returnStatus, String returnToken,
			String supplierCode) {
		return ReturnPackageRequestDto.builder()
                .returnStatus(returnStatus)
                .returnToken(returnToken)
                .parcel(createParcel())
                .reason("Recipient not available")
                .supplierCode(createSupplierCode(supplierCode))
                .build();
	}

    private SupplierCodeDto createSupplierCode(String supplierCode) {
        return SupplierCodeDto.builder().value(supplierCode).build();
    }

    private ParcelDto createParcel() {
        return ParcelDto.builder()
                .parcelSize(SizeDto.TEST)
                .parcelType(ParcelTypeDto.PARENT)
                .parcelRelatedId(null)
                .id(1L)
                .destination("KT1")
                .build();
    }
}
