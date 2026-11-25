package com.warehouse.billing.invoice.application.port.secondary;

import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.vo.InvoiceDocument;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceDocumentServicePort {

    UUID store(final Invoice invoice, final UserId uploadedBy);

    Optional<InvoiceDocument> load(final UUID documentId);
}
