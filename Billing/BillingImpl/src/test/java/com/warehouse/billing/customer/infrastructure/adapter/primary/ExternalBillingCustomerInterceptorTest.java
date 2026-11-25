package com.warehouse.billing.customer.infrastructure.adapter.primary;

import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.configuration.BillingCustomerWebConfiguration;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(ExternalBillingCustomerInterceptorTest.TestWebConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExternalBillingCustomerInterceptorTest {

    private final BillingCustomerPort billingCustomerPort;

    private final StubController stubController;

    private final MockMvc mockMvc;

    ExternalBillingCustomerInterceptorTest(final WebApplicationContext context,
                                           final BillingCustomerPort billingCustomerPort,
                                           final StubController stubController) {
        this.billingCustomerPort = billingCustomerPort;
        this.stubController = stubController;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeEach
    void setUp() {
        reset(this.billingCustomerPort);
        this.stubController.calls.set(0);
    }

    @Test
    void shouldRejectBlockedCustomerOnBothExternalEndpoints() throws Exception {
        when(this.billingCustomerPort.currentActive()).thenThrow(new BillingCustomerException(
                BillingCustomerException.Reason.NOT_ACTIVE, "Business customer is not active"));

        this.mockMvc.perform(post("/external/shipments")).andExpect(status().isForbidden());
        this.mockMvc.perform(get("/external/payments/TRK-1")).andExpect(status().isForbidden());

        assertThat(this.stubController.calls.get()).isZero();
    }

    @Test
    void shouldLetActiveCustomerThrough() throws Exception {
        when(this.billingCustomerPort.currentActive()).thenReturn(mock(BillingCustomer.class));

        this.mockMvc.perform(post("/external/shipments")).andExpect(status().isOk());
        this.mockMvc.perform(get("/external/payments/TRK-1")).andExpect(status().isOk());

        assertThat(this.stubController.calls.get()).isEqualTo(2);
    }

    @Test
    void shouldNotGuardInternalEndpoints() throws Exception {
        this.mockMvc.perform(get("/billing/customers")).andExpect(status().isOk());

        verifyNoInteractions(this.billingCustomerPort);
    }

    @RestController
    static class StubController {

        private final AtomicInteger calls = new AtomicInteger();

        @PostMapping("/external/shipments")
        String createShipment() {
            this.calls.incrementAndGet();
            return "ok";
        }

        @GetMapping("/external/payments/{trackingNumber}")
        String payment(@PathVariable final String trackingNumber) {
            this.calls.incrementAndGet();
            return "ok";
        }

        @GetMapping("/billing/customers")
        String customers() {
            return "ok";
        }
    }

    @Configuration
    @EnableWebMvc
    @Import(BillingCustomerWebConfiguration.class)
    static class TestWebConfiguration {

        @Bean
        BillingCustomerPort billingCustomerPort() {
            return mock(BillingCustomerPort.class);
        }

        @Bean
        StubController stubController() {
            return new StubController();
        }
    }
}
