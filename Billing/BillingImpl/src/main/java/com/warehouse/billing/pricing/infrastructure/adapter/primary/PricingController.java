package com.warehouse.billing.pricing.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceListApiResponse;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceQuoteApiRequest;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceQuoteApiResponse;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.ReplacePriceListApiRequest;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.mapper.PricingWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AccessUserControl
@RestController
@RequestMapping("/billing")
public class PricingController {

    private final PricingPort pricingPort;

    private final PricingWebMapper pricingWebMapper;

    public PricingController(final PricingPort pricingPort, final PricingWebMapper pricingWebMapper) {
        this.pricingPort = pricingPort;
        this.pricingWebMapper = pricingWebMapper;
    }

    @GetMapping("/price-list")
    public ResponseEntity<PriceListApiResponse> getPriceList() {
        return ResponseEntity.ok(this.pricingWebMapper.toResponse(this.pricingPort.getPriceList()));
    }

    @PutMapping("/price-list")
    public ResponseEntity<PriceListApiResponse> replacePriceList(
            @Valid @RequestBody final ReplacePriceListApiRequest request) {
        return ResponseEntity.ok(this.pricingWebMapper.toResponse(
                this.pricingPort.replacePriceList(this.pricingWebMapper.toCommand(request))));
    }

    @PostMapping("/quotes")
    public ResponseEntity<PriceQuoteApiResponse> previewQuote(@Valid @RequestBody final PriceQuoteApiRequest request) {
        return ResponseEntity.ok(this.pricingWebMapper.toResponse(
                this.pricingPort.preview(this.pricingWebMapper.toCommand(request))));
    }
}
