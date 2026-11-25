package com.warehouse.billing.invoice.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public record InvoiceLineApi(String description,
                             BigDecimal vatPercent,
                             BigDecimal net,
                             BigDecimal vat,
                             BigDecimal gross) {
}
