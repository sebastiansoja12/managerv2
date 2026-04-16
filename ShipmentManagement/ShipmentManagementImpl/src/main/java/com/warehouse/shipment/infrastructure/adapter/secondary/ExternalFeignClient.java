package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.*;

import feign.RequestInterceptor;

@FeignClient(
        name = "external-microservice-client",
        configuration = ExternalFeignClient.Configuration.class
)
public interface ExternalFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReturnResponseApi> processReturn(final URI uri, @RequestBody final ReturnRequestApi request);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> completeReturn(final URI uri, @RequestBody final ChangeReturnStatusApiRequest request);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RouteProcessDto> createShipmentRoute(final URI uri, @RequestBody final ShipmentCreatedRequest request);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RouteLogRecordDto> updateRoutePerson(final URI uri,
                                                        @RequestHeader("X-API-KEY") final String apiKey,
                                                        @RequestBody final PersonChangedRequest request);

    class Configuration {

        @Bean
        RequestInterceptor externalMicroserviceAuthenticationRequestInterceptor(final CurrentUserApiService currentUserApiService) {
            return requestTemplate -> {
                final CurrentUserAuthenticationDto authentication = currentUserApiService.getCurrentUserAuthentication();
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + authentication.jwtToken());
            };
        }
    }
}
