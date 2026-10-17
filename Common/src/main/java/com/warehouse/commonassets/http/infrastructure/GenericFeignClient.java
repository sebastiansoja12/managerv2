package com.warehouse.commonassets.http.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "generic-external-service-client")
public interface GenericFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> post(final URI uri,
                              @RequestHeader(HttpHeaders.AUTHORIZATION) final String authorizationHeader,
                              @RequestBody final Object request);
}
