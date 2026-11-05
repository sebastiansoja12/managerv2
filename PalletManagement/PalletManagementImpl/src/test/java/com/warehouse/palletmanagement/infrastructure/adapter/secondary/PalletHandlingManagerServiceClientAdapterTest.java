package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.commonassets.resilience.CircuitBreakerExecutor;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.exception.PalletServiceUnavailableException;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.mapper.PalletHandlingManagerResponseMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PalletHandlingManagerServiceClientAdapterTest {

    private static final String BASE_URL = "http://pallet-handling-manager";
    private static final String PALLET_JSON = """
            {
              "palletId": "pallet-1",
              "trackingNumber": "PAL000000000001",
              "operatorId": 7,
              "palletStatus": "READY",
              "storageStatus": "UNLOCKED",
              "shipmentIds": [1, 2],
              "empty": false,
              "loadable": true,
              "loadBlockReason": null,
              "grossWeight": {"value": 120.5, "unit": "KG"},
              "maxGrossWeight": null,
              "refrigerated": false,
              "created": "2026-10-17T10:00:00Z",
              "modified": "2026-10-17T10:05:00Z"
            }
            """;

    private MockRestServiceServer server;
    private CircuitBreakerRegistry circuitBreakerRegistry;
    private PalletHandlingManagerServiceClientAdapter adapter;

    @BeforeEach
    void setUp() {
        final RestClient.Builder restClientBuilder = RestClient.builder();
        this.server = MockRestServiceServer.bindTo(restClientBuilder).build();
        this.circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        this.circuitBreakerRegistry.circuitBreaker(PalletHandlingManagerServiceClientAdapter.CIRCUIT_BREAKER_NAME,
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(2)
                        .minimumNumberOfCalls(2)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofMinutes(1))
                        .recordExceptions(ResourceAccessException.class, PalletHandlingManagerServerException.class)
                        .build());
        final Retry readRetry = Retry.of("pallet-read", RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(10))
                .retryExceptions(ResourceAccessException.class, PalletHandlingManagerServerException.class)
                .build());
        final CurrentUserApiService currentUserApiService = mock(CurrentUserApiService.class);
        when(currentUserApiService.getCurrentUserAuthentication())
                .thenReturn(new CurrentUserAuthenticationDto("user-jwt", null));
        final ObjectMapper objectMapper = JsonMapper.builder().configureForJackson2().build();
        this.adapter = new PalletHandlingManagerServiceClientAdapter(restClientBuilder, BASE_URL,
                new CircuitBreakerExecutor(this.circuitBreakerRegistry), readRetry, currentUserApiService,
                objectMapper, new PalletHandlingManagerResponseMapper());
    }

    @Test
    void shouldFindPalletByTrackingNumberWithUserJwt() {
        this.server.expect(requestTo(BASE_URL + "/pallets/tracking/PAL000000000001"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer user-jwt"))
                .andRespond(withSuccess(PALLET_JSON, MediaType.APPLICATION_JSON));

        final PalletDetails pallet = this.adapter.getByTrackingNumber(new PalletTrackingNumber("pal000000000001"));

        this.server.verify();
        assertThat(pallet.palletId()).isEqualTo(new PalletId("pallet-1"));
        assertThat(pallet.palletStatus()).isEqualTo(PalletStatus.READY);
        assertThat(pallet.shipmentIds()).containsExactly(new ShipmentId(1L), new ShipmentId(2L));
        assertThat(pallet.grossWeight().value()).isEqualByComparingTo("120.5");
    }

    @Test
    void shouldMapRejectedRequestWithoutOpeningCircuitBreaker() {
        for (int index = 0; index < 3; index++) {
            this.server.expect(requestTo(BASE_URL + "/pallets/tracking/PAL404"))
                    .andRespond(withStatus(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"code\":\"PALLET_NOT_FOUND\",\"message\":\"Pallet not found\"}"));
        }

        for (int index = 0; index < 3; index++) {
            assertThatThrownBy(() -> this.adapter.getByTrackingNumber(new PalletTrackingNumber("PAL404")))
                    .isInstanceOfSatisfying(PalletRequestRejectedException.class, exception -> {
                        assertThat(exception.getCode()).isEqualTo("PALLET_NOT_FOUND");
                        assertThat(exception.getStatus()).isEqualTo(404);
                    });
        }

        this.server.verify();
        assertThat(circuitBreaker().getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    void shouldRetryReadAfterServerError() {
        this.server.expect(requestTo(BASE_URL + "/pallets/tracking/PAL000000000001"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
        this.server.expect(requestTo(BASE_URL + "/pallets/tracking/PAL000000000001"))
                .andRespond(withSuccess(PALLET_JSON, MediaType.APPLICATION_JSON));

        final PalletDetails pallet = this.adapter.getByTrackingNumber(new PalletTrackingNumber("PAL000000000001"));

        this.server.verify();
        assertThat(pallet.trackingNumber().value()).isEqualTo("PAL000000000001");
    }

    @Test
    void shouldOpenCircuitBreakerAndFailFastWhenServiceIsDown() {
        this.server.expect(requestTo(BASE_URL + "/pallets"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
        this.server.expect(requestTo(BASE_URL + "/pallets"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(this.adapter::createEmptyPallet).isInstanceOf(PalletServiceUnavailableException.class);
        assertThatThrownBy(this.adapter::createEmptyPallet).isInstanceOf(PalletServiceUnavailableException.class);
        assertThat(circuitBreaker().getState()).isEqualTo(CircuitBreaker.State.OPEN);

        assertThatThrownBy(this.adapter::createEmptyPallet).isInstanceOf(PalletServiceUnavailableException.class);
        this.server.verify();
    }

    @Test
    void shouldCreatePalletFromShipments() {
        this.server.expect(requestTo(BASE_URL + "/pallets/shipments"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer user-jwt"))
                .andExpect(content().json("{\"shipmentIds\":[1,2]}"))
                .andRespond(withSuccess(PALLET_JSON, MediaType.APPLICATION_JSON));

        final PalletDetails pallet = this.adapter.createPallet(List.of(new ShipmentId(1L), new ShipmentId(2L)));

        this.server.verify();
        assertThat(pallet.loadable()).isTrue();
    }

    private CircuitBreaker circuitBreaker() {
        return this.circuitBreakerRegistry.circuitBreaker(PalletHandlingManagerServiceClientAdapter.CIRCUIT_BREAKER_NAME);
    }
}
