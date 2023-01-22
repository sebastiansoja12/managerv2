package com.warehouse.delivery.infrastructure.adapter.primary;

import com.warehouse.delivery.DeliveryService;
import com.warehouse.route.infrastructure.api.dto.SupplyInformationDto;
import com.warehouse.supplier.dto.SupplyResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v2/api/deliveries")
@RestController
@AllArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;


    @PostMapping
    public SupplyResponseDto createSupply(@RequestBody SupplyInformationDto supplyInformation) {
        return deliveryService.deliver(supplyInformation);
    }
}
