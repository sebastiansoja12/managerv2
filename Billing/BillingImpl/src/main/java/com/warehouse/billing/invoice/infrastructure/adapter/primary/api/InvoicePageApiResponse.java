package com.warehouse.billing.invoice.infrastructure.adapter.primary.api;

import java.util.List;

public record InvoicePageApiResponse(List<InvoiceApiResponse> invoices,
                                     int page,
                                     int size,
                                     long totalElements) {
}
