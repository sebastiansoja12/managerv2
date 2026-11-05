package com.warehouse.geocoding.infrastructure.adapter.primary;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.geocoding.application.port.primary.PostalCodePort;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.PostalCodeApiResponse;

@RestController
@RequestMapping("/postal-codes")
public class PostalCodeController {

    private final PostalCodePort postalCodePort;

    public PostalCodeController(final PostalCodePort postalCodePort) {
        this.postalCodePort = postalCodePort;
    }

    @GetMapping
    public ResponseEntity<List<PostalCodeApiResponse>> search(@RequestParam("query") final String query) {
        return ResponseEntity.ok(postalCodePort.search(query)
                .stream()
                .map(PostalCodeApiResponse::from)
                .toList());
    }
}
