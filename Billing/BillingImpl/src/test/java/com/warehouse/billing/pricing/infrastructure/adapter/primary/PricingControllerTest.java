package com.warehouse.billing.pricing.infrastructure.adapter.primary;

import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.application.port.primary.command.QuoteShipmentCommand;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.mapper.PricingWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PricingControllerTest {

    private PricingPort pricingPort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.pricingPort = mock(PricingPort.class);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new PricingController(this.pricingPort, new PricingWebMapper()))
                .setControllerAdvice(new PricingExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn422WhenShipmentNotPriceable() throws Exception {
        when(this.pricingPort.preview(any(QuoteShipmentCommand.class)))
                .thenThrow(new ShipmentNotPriceableException("Shipment exceeds every size class"));

        this.mockMvc.perform(post("/billing/quotes").contentType(MediaType.APPLICATION_JSON).content("""
                        {"lengthCm":500,"widthCm":500,"heightCm":500,"weightKg":2,"serviceLevel":"STANDARD",
                         "deliveryOption":"COURIER"}"""))
                .andExpect(status().isUnprocessableEntity());
    }
}
