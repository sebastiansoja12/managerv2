package com.warehouse.billing.customer.infrastructure.adapter.primary;

import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.application.port.primary.command.CreateBillingCustomerCommand;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.domain.service.BillingCustomerFactory;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAccount;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAddress;
import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.billing.customer.infrastructure.adapter.primary.mapper.BillingCustomerWebMapper;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BillingCustomerControllerTest {

    private static final Instant NOW = Instant.parse("2026-11-24T12:00:00Z");

    private static final String BODY = """
            {"companyName":"Sklep ABC","taxId":"5260250995","street":"Długa 1",
             "postalCode":"31-147","city":"Kraków","countryCode":"PL",
             "invoiceEmail":"faktury@sklep.pl"}""";

    private BillingCustomerPort billingCustomerPort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.billingCustomerPort = mock(BillingCustomerPort.class);
        final FormattingConversionService conversion = new FormattingConversionService();
        conversion.addConverter(String.class, BillingCustomerId.class,
                value -> new BillingCustomerId(Long.valueOf(value)));
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new BillingCustomerController(this.billingCustomerPort,
                        new BillingCustomerWebMapper()))
                .setControllerAdvice(new BillingCustomerExceptionHandler())
                .setConversionService(conversion)
                .build();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        when(this.billingCustomerPort.create(any())).thenReturn(customer());

        this.mockMvc.perform(post("/billing/customers").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"companyName":"Sklep ABC","taxId":"5260250995","street":"Długa 1",
                                 "postalCode":"31-147","city":"Kraków","countryCode":"PL",
                                 "invoiceEmail":"faktury@sklep.pl"}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxId").value("5260250995"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.userId.value").value(70))
                .andExpect(jsonPath("$.apiKey").doesNotExist());

        final ArgumentCaptor<CreateBillingCustomerCommand> command =
                ArgumentCaptor.forClass(CreateBillingCustomerCommand.class);
        verify(this.billingCustomerPort).create(command.capture());
        assertThat(command.getValue().taxId()).isEqualTo(new TaxId("5260250995"));
        assertThat(command.getValue().details().invoiceEmail()).isEqualTo("faktury@sklep.pl");
    }

    @Test
    void shouldRejectInvalidTaxId() throws Exception {
        this.mockMvc.perform(post("/billing/customers").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"companyName":"Sklep ABC","taxId":"52 60/250995","street":"Długa 1",
                                 "postalCode":"31-147","city":"Kraków","countryCode":"PL",
                                 "invoiceEmail":"faktury@sklep.pl"}"""))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.billingCustomerPort);
    }

    @Test
    void shouldReturnApiKeyOnceWithoutCaching() throws Exception {
        when(this.billingCustomerPort.regenerateApiKey(new BillingCustomerId(5L))).thenReturn("new-api-key");

        this.mockMvc.perform(post("/billing/customers/5/api-key"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "no-store"))
                .andExpect(jsonPath("$.apiKey").value("new-api-key"));
    }

    @Test
    void shouldBlockCustomer() throws Exception {
        final BillingCustomer customer = customer();
        customer.block(NOW);
        when(this.billingCustomerPort.block(new BillingCustomerId(5L))).thenReturn(customer);

        this.mockMvc.perform(post("/billing/customers/5/block"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    void shouldRejectDuplicateTaxIdInOperatorWith409() throws Exception {
        when(this.billingCustomerPort.create(any())).thenThrow(new BillingCustomerException(
                BillingCustomerException.Reason.DUPLICATE, "Business customer with this tax id already exists"));

        this.mockMvc.perform(post("/billing/customers").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Business customer with this tax id already exists"));
    }

    @Test
    void shouldMapConstraintViolationOnCreateTo409() throws Exception {
        when(this.billingCustomerPort.create(any())).thenThrow(
                new DataIntegrityViolationException("duplicate key value violates unique constraint"));

        this.mockMvc.perform(post("/billing/customers").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Business customer with this tax id already exists"));
    }

    private BillingCustomer customer() {
        return new BillingCustomerFactory().create(new TaxId("5260250995"),
                new BillingCustomerDetails("Sklep ABC", new BillingCustomerAddress("Długa 1", "31-147", "Kraków",
                        "PL"), "faktury@sklep.pl"),
                new BillingCustomerAccount(new UserId(70L), OperatorId.of(7L)), NOW);
    }
}
