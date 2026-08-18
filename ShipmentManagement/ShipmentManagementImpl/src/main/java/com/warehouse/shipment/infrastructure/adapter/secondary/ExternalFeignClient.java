package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.*;

import feign.RequestInterceptor;

@FeignClient(
        name = "external-microservice-client",
        configuration = ExternalFeignClient.Configuration.class
)
public interface ExternalFeignClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReturnPackageApi> getReturn(final URI uri);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReturnPageApi> getReturns(final URI uri);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReturnResponseApi> processReturn(final URI uri, @RequestBody final ReturnRequestApi request);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> completeReturn(final URI uri, @RequestBody final ChangeReturnStatusApiRequest request);

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
