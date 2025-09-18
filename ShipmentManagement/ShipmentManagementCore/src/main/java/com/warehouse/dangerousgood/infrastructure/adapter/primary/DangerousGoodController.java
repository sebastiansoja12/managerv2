package com.warehouse.dangerousgood.infrastructure.adapter.primary;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.dangerousgood.domain.model.DangerousGoodCreateRequest;
import com.warehouse.dangerousgood.domain.port.primary.DangerousGoodPort;
import com.warehouse.dangerousgood.infrastructure.adapter.primary.api.DangerousGoodCreateApiRequest;

@RestController
@RequestMapping("/dangerous-goods")
public class DangerousGoodController {

    private final DangerousGoodPort dangerousGoodPort;

    public DangerousGoodController(final DangerousGoodPort dangerousGoodPort) {
        this.dangerousGoodPort = dangerousGoodPort;
    }

    @PostMapping
    public ResponseEntity<?> createDangerousGood(@RequestBody final DangerousGoodCreateApiRequest request) {
        final DangerousGoodCreateRequest dangerousGoodCreateRequest = DangerousGoodCreateRequest.from(request);
        this.dangerousGoodPort.createDangerousGood(dangerousGoodCreateRequest);
        return ResponseEntity.ok().build();
    }

}
