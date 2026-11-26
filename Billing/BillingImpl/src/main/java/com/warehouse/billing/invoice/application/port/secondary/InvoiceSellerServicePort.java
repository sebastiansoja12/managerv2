package com.warehouse.billing.invoice.application.port.secondary;

import com.warehouse.billing.invoice.domain.vo.InvoiceSeller;

public interface InvoiceSellerServicePort {

    InvoiceSeller currentSeller();
}
