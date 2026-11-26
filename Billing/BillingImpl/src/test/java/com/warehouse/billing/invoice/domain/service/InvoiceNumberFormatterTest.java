package com.warehouse.billing.invoice.domain.service;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceNumberFormatterTest {

    private final InvoiceNumberFormatter formatter = new InvoiceNumberFormatter();

    @Test
    void shouldFormatInvoiceNumbers() {
        assertThat(this.formatter.format(InvoiceType.INVOICE, YearMonth.of(2026, 11), 1))
                .isEqualTo("FV/2026/11/0001");
        assertThat(this.formatter.format(InvoiceType.CORRECTION, YearMonth.of(2026, 11), 1))
                .isEqualTo("KOR/2026/11/0001");
        assertThat(this.formatter.format(InvoiceType.INVOICE, YearMonth.of(2027, 2), 42))
                .isEqualTo("FV/2027/02/0042");
        assertThat(this.formatter.format(InvoiceType.INVOICE, YearMonth.of(2026, 12), 12345))
                .isEqualTo("FV/2026/12/12345");
    }
}
