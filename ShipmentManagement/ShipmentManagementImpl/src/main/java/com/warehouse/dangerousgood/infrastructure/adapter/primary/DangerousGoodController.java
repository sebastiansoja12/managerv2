package com.warehouse.dangerousgood.infrastructure.adapter.primary;


import com.warehouse.dangerousgood.domain.model.DangerousGoodChangeWeightRequest;
import com.warehouse.dangerousgood.infrastructure.adapter.primary.api.DangerousGoodChangeWeightApiRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/weight")
    public ResponseEntity<?> updateDangerousGoodWeight(@RequestBody final DangerousGoodChangeWeightApiRequest request) {
        final DangerousGoodChangeWeightRequest dangerousGoodChangeWeightRequest = DangerousGoodChangeWeightRequest.from(request);
        this.dangerousGoodPort.updateDangerousGoodWeight(dangerousGoodChangeWeightRequest);
        return ResponseEntity.ok().build();
    }

}
