package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.SupplierCodeLogServicePort;
import com.warehouse.deliveryreturn.domain.vo.SupplierCodeRequest;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class SupplierCodeLogServiceAdapter implements SupplierCodeLogServicePort {

    private final RouteTrackerLogProperties routeTrackerLogProperties;

    private final RestClient restClient;

    @Override
    public void saveSupplierCode(SupplierCodeRequest request) {
        restClient.post()
                .uri("/v2/api/routes/{endpoint}", routeTrackerLogProperties.getSupplierCode())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully registered return in tracker module"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while logging return"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while logging return"));
    }
}
