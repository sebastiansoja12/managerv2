package com.warehouse.billing.invoice.infrastructure.adapter.primary;

import com.warehouse.billing.invoice.application.port.primary.InvoicePort;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.model.InvoiceFixture;
import com.warehouse.billing.invoice.domain.vo.InvoiceDocument;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper.InvoiceWebMapper;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvoiceControllerTest {

    private InvoicePort invoicePort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.invoicePort = mock(InvoicePort.class);
        final FormattingConversionService conversion = new FormattingConversionService();
        conversion.addConverter(String.class, InvoiceId.class, value -> new InvoiceId(Long.valueOf(value)));
        conversion.addConverter(String.class, PriceQuoteId.class, value -> new PriceQuoteId(Long.valueOf(value)));
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new InvoiceController(this.invoicePort, new InvoiceWebMapper()))
                .setControllerAdvice(new InvoiceExceptionHandler())
                .setConversionService(conversion)
                .build();
    }

    @Test
    void shouldListInvoices() throws Exception {
        final Invoice invoice = InvoiceFixture.invoice(new PaymentId(1L), 1);
        when(this.invoicePort.find(any())).thenReturn(new InvoicePage(List.of(invoice), 0, 20, 1));

        this.mockMvc.perform(get("/billing/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoices[0].invoiceId.value").value(invoice.id().value()))
                .andExpect(jsonPath("$.invoices[0].number").value("FV/2026/11/0001"))
                .andExpect(jsonPath("$.invoices[0].paymentId.value").value(1))
                .andExpect(jsonPath("$.invoices[0].gross").value(18.45))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldReturn400WhenPageSizeIsOutOfBounds() throws Exception {
        this.mockMvc.perform(get("/billing/invoices").param("size", "101"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/billing/invoices").param("page", "-1"))
                .andExpect(status().isBadRequest());

        verify(this.invoicePort, never()).find(any());
    }

    @Test
    void shouldDownloadInvoicePdf() throws Exception {
        final byte[] pdf = {0x25, 0x50, 0x44, 0x46};
        when(this.invoicePort.findDocument(new InvoiceId(9L)))
                .thenReturn(Optional.of(new InvoiceDocument("FV_2026_11_0001.pdf", pdf)));

        this.mockMvc.perform(get("/billing/invoices/9/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"FV_2026_11_0001.pdf\""))
                .andExpect(content().bytes(pdf));
    }

    @Test
    void shouldReturn404WhenInvoicePdfIsMissing() throws Exception {
        when(this.invoicePort.findDocument(new InvoiceId(9L))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/billing/invoices/9/pdf"))
                .andExpect(status().isNotFound());
    }
}
