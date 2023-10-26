package com.warehouse.returning;


import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.warehouse.exceptionhandler.model.ErrorResponse;
import com.warehouse.returning.infrastructure.api.dto.*;

@SpringBootTest(classes = ReturningIntegrationTest.ReturningTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
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

    @Test
    void shouldProcessReturning() throws Exception {
        // given
        final String request = readFileAsString(RETURN_REQUEST_JSON_PATH);
        // when
        final ResponseEntity<ReturningResponseDto> response = restClient.post()
                .uri("/v2/api/returns")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ReturningResponseDto.class);

        // then
        assertNotNull(response);
        assertEquals(returningResponseAsStringJson(), objectMapper.writeValueAsString(response.getBody()));
    }

    @Test
    void shouldFailWhenUserIsMissing() throws Exception {
        // given
        final ReturningRequestDto returningRequest = createReturningRequest("", "KT1", "returnToken",
                "abc");
        // when
        final ResponseEntity<ErrorResponse> returningResponse = restClient.post()
                .uri("/v2/api/returns")
                .body(returningRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(ErrorResponse.class);

        // then
        assertTrue(returningResponse.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST));
        assertNotNull(returningResponse.getBody());
        assertEquals(readFileAsString(USERNAME_MISSING_JSON_PATH),
                returningResponseAsStringJson(returningResponse.getBody()));
    }

    @Test
    void shouldFailWhenDepotIsMissing() throws Exception {
        // given
        final ReturningRequestDto returningRequest = createReturningRequest("s-soja", "", "returnToken",
                "abc");
        // when
        final ResponseEntity<ErrorResponse> returningResponse = restClient.post()
                .uri("/v2/api/returns")
                .body(returningRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(ErrorResponse.class);

        // then
        assertTrue(returningResponse.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST));
        assertNotNull(returningResponse.getBody());
        assertEquals(readFileAsString(DEPOT_CODE_MISSING_JSON_PATH),
                returningResponseAsStringJson(returningResponse.getBody()));
    }
    
    private static String returningResponseAsStringJson(ErrorResponse errorResponse) {
		final String json = """
				{
				  "status": %s,
				  "error": "%s"
				}""";

        return String.format(json, errorResponse.getStatus(), errorResponse.getError());
    }

    private static String returningResponseAsStringJson() {
        return """
                {
                  "processReturn" : [ {
                    "returnId" : 1,
                    "processStatus" : "PROCESSING"
                  }, {
                    "returnId" : 2,
                    "processStatus" : "PROCESSING"
                  } ]
                }""";
    }

    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

	private ReturningRequestDto createReturningRequest(String username, String depotCode, String returnToken,
			String supplierCode) {
        final ReturningRequestDto request = new ReturningRequestDto();
        request.setRequests(createReturnPackageRequests(ReturnStatusDto.CREATED, returnToken, supplierCode));
        request.setUsername(createUsername(username));
        request.setDepotCode(createDepotCode(depotCode));
        return request;
    }

    private UsernameDto createUsername(String username) {
        final UsernameDto user = new UsernameDto();
        user.setValue(username);
        return user;
    }

    private DepotCodeDto createDepotCode(String depotCode) {
        final DepotCodeDto depot = new DepotCodeDto();
        depot.setValue(depotCode);
        return depot;
    }

    private List<ReturnPackageRequestDto> createReturnPackageRequests(ReturnStatusDto returnStatusDto,
			String returnToken, String supplierCode) {
		return List.of(createReturnPackageRequest(returnStatusDto, returnToken, supplierCode));
	}

	private ReturnPackageRequestDto createReturnPackageRequest(ReturnStatusDto returnStatus, String returnToken,
			String supplierCode) {
		final ReturnPackageRequestDto returnPackageRequest = new ReturnPackageRequestDto();
		returnPackageRequest.setReturnStatus(returnStatus);
		returnPackageRequest.setReturnToken(returnToken);
		returnPackageRequest.setParcel(createParcel());
		returnPackageRequest.setReason("Recipient not available");
		returnPackageRequest.setSupplierCode(createSupplierCode(supplierCode));
		return returnPackageRequest;
	}

    private SupplierCodeDto createSupplierCode(String supplierCode) {
        final SupplierCodeDto supplier = new SupplierCodeDto();
        supplier.setValue(supplierCode);
        return supplier;
    }

    private ParcelDto createParcel() {
        final ParcelDto parcel = new ParcelDto();
        parcel.setParcelSize(SizeDto.TEST);
        parcel.setParcelType(ParcelTypeDto.PARENT);
        parcel.setId(1L);
        parcel.setParcelRelatedId(null);
        parcel.setDestination("KT1");
        return parcel;
    }
}
