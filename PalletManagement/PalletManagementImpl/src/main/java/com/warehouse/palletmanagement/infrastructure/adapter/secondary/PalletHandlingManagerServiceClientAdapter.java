package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.palletmanagement.domain.vo.PalletUpdate;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingStatusRequestApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.resilience.CircuitBreakerExecutor;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.exception.PalletServiceUnavailableException;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChange;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingHistoryEntryApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingMaxWeightRequestApi;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletPage;
import com.warehouse.palletmanagement.domain.vo.PalletSearchCriteria;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingErrorApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingPageApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingPalletApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.mapper.PalletHandlingManagerResponseMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.function.Supplier;

public class PalletHandlingManagerServiceClientAdapter implements PalletHandlingManagerServicePort {

    public static final String CIRCUIT_BREAKER_NAME = "pallet-handling-manager";

    private static final Logger log = LoggerFactory.getLogger(PalletHandlingManagerServiceClientAdapter.class);
    private static final String PALLETS = "/pallets";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String UNAVAILABLE_MESSAGE = "Pallet service is temporarily unavailable";

    private final RestClient restClient;
    private final String baseUrl;
    private final CircuitBreakerExecutor circuitBreakerExecutor;
    private final Retry readRetry;
    private final CurrentUserApiService currentUserApiService;
    private final ObjectMapper objectMapper;
    private final PalletHandlingManagerResponseMapper responseMapper;

    public PalletHandlingManagerServiceClientAdapter(final RestClient.Builder restClientBuilder,
                                                     final String baseUrl,
                                                     final CircuitBreakerExecutor circuitBreakerExecutor,
                                                     final Retry readRetry,
                                                     final CurrentUserApiService currentUserApiService,
                                                     final ObjectMapper objectMapper,
                                                     final PalletHandlingManagerResponseMapper responseMapper) {
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder.clone()
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw rejected(response);
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new PalletHandlingManagerServerException(
                            "Pallet service responded with status " + response.getStatusCode().value());
                })
                .build();
        this.baseUrl = baseUrl;
        this.circuitBreakerExecutor = circuitBreakerExecutor;
        this.readRetry = readRetry;
        this.currentUserApiService = currentUserApiService;
        this.responseMapper = responseMapper;
    }

    @Override
    public PalletDetails createEmptyPallet() {
        final String authorization = authorization();
        return this.responseMapper.map(write(() -> this.restClient.post()
                .uri(this.baseUrl + PALLETS)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails createPallet(final List<ShipmentId> shipmentIds) {
        final String authorization = authorization();
        return this.responseMapper.map(write(() -> this.restClient.post()
                .uri(this.baseUrl + PALLETS + "/shipments")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.responseMapper.map(shipmentIds))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails getByTrackingNumber(final PalletTrackingNumber trackingNumber) {
        final String authorization = authorization();
        final URI uri = UriComponentsBuilder.fromUriString(this.baseUrl + PALLETS + "/tracking/{trackingNumber}")
                .buildAndExpand(trackingNumber.value())
                .encode()
                .toUri();
        return this.responseMapper.map(read(() -> this.restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletPage findPallets(final PalletSearchCriteria criteria) {
        final String authorization = authorization();
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(this.baseUrl + PALLETS)
                .queryParam("empty", criteria.onlyEmpty())
                .queryParam("page", criteria.page())
                .queryParam("size", criteria.size());
        criteria.status().ifPresent(status -> uriBuilder.queryParam("status", status.name()));
        final URI uri = uriBuilder.build().encode().toUri();
        return this.responseMapper.map(read(() -> this.restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingPageApi.class)));
    }

    @Override
    public PalletDetails cancelPallet(final PalletId palletId) {
        final String authorization = authorization();
        final URI uri = UriComponentsBuilder.fromUriString(this.baseUrl + PALLETS + "/{palletId}/cancel")
                .buildAndExpand(palletId.value())
                .encode()
                .toUri();
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails addShipments(final PalletId palletId, final List<ShipmentId> shipmentIds) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/shipments", palletId.value());
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.responseMapper.map(shipmentIds))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails removeShipment(final PalletId palletId, final ShipmentId shipmentId) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/shipments/{shipmentId}", palletId.value(), shipmentId.getValue());
        return this.responseMapper.map(write(() -> this.restClient.delete()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails changeGrossWeight(final PalletId palletId, final PalletWeight grossWeight) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/weight", palletId.value());
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.responseMapper.map(grossWeight))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/max-weight", palletId.value());
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PalletHandlingMaxWeightRequestApi(maxGrossWeight))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public List<PalletHistoryEntry> getHistory(final PalletId palletId) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/history", palletId.value());
        final PalletHandlingHistoryEntryApi[] history = read(() -> this.restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingHistoryEntryApi[].class));
        return this.responseMapper.map(history);
    }

    @Override
    public PalletDetails changeDepartments(final PalletId palletId, final PalletDepartmentsChange departmentsChange) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/departments", palletId.value());
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.responseMapper.map(departmentsChange))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails getPallet(final PalletId palletId) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}", palletId.value());
        return this.responseMapper.map(read(() -> this.restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails updatePallet(final PalletId palletId, final PalletUpdate update) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}", palletId.value());
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.responseMapper.map(update))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    @Override
    public PalletDetails changeStatus(final PalletId palletId, final PalletStatus targetStatus) {
        final String authorization = authorization();
        final URI uri = palletUri("/{palletId}/status", palletId.value());
        return this.responseMapper.map(write(() -> this.restClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PalletHandlingStatusRequestApi(targetStatus.name()))
                .retrieve()
                .body(PalletHandlingPalletApi.class)));
    }

    private URI palletUri(final String path, final Object... variables) {
        return UriComponentsBuilder.fromUriString(this.baseUrl + PALLETS + path)
                .buildAndExpand(variables)
                .encode()
                .toUri();
    }

    private <T> T read(final Supplier<T> call) {
        return guard(() -> this.readRetry.executeSupplier(
                () -> this.circuitBreakerExecutor.execute(CIRCUIT_BREAKER_NAME, call)));
    }

    private <T> T write(final Supplier<T> call) {
        return guard(() -> this.circuitBreakerExecutor.execute(CIRCUIT_BREAKER_NAME, call));
    }

    private <T> T guard(final Supplier<T> call) {
        try {
            return call.get();
        } catch (final CallNotPermittedException exception) {
            log.warn("Pallet service circuit breaker is open, request rejected");
            throw new PalletServiceUnavailableException(UNAVAILABLE_MESSAGE);
        } catch (final ResourceAccessException | PalletHandlingManagerServerException exception) {
            log.warn("Pallet service call failed: {}", exception.getMessage());
            throw new PalletServiceUnavailableException(UNAVAILABLE_MESSAGE);
        }
    }

    private String authorization() {
        return BEARER_PREFIX + this.currentUserApiService.getCurrentUserAuthentication().jwtToken();
    }

    private PalletRequestRejectedException rejected(final ClientHttpResponse response) throws IOException {
        final int status = response.getStatusCode().value();
        try {
            final PalletHandlingErrorApi error = this.objectMapper.readValue(response.getBody(),
                    PalletHandlingErrorApi.class);
            return new PalletRequestRejectedException(status,
                    error.code() == null ? "PALLET_REQUEST_REJECTED" : error.code(),
                    error.message() == null ? "Pallet request rejected" : error.message());
        } catch (final IOException exception) {
            return new PalletRequestRejectedException(status, "PALLET_REQUEST_REJECTED", "Pallet request rejected");
        }
    }
}
