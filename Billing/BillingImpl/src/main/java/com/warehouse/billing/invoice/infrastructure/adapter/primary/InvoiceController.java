package com.warehouse.billing.invoice.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.billing.invoice.application.port.primary.InvoicePort;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.api.InvoicePageApiResponse;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper.InvoiceWebMapper;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AccessUserControl
@RestController
@RequestMapping("/billing/invoices")
public class InvoiceController {

    private final InvoicePort invoicePort;

    private final InvoiceWebMapper invoiceWebMapper;

    public InvoiceController(final InvoicePort invoicePort, final InvoiceWebMapper invoiceWebMapper) {
        this.invoicePort = invoicePort;
        this.invoiceWebMapper = invoiceWebMapper;
    }

    @GetMapping
    public ResponseEntity<InvoicePageApiResponse> find(
            @RequestParam(required = false) final PriceQuoteId priceQuoteId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        return ResponseEntity.ok(this.invoiceWebMapper.toResponse(this.invoicePort.find(
                this.invoiceWebMapper.toFindCommand(priceQuoteId, page, size))));
    }

    @GetMapping("/{invoiceId}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable final InvoiceId invoiceId) {
        return this.invoicePort.findDocument(invoiceId)
                .map(document -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                ContentDisposition.attachment().filename(document.fileName()).build().toString())
                        .body(document.content()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
