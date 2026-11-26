package com.warehouse.billing.invoice.application.listener;

import com.warehouse.billing.api.event.InvoiceIssuedIntegrationEvent;
import com.warehouse.billing.invoice.domain.event.InvoiceIssued;
import com.warehouse.billing.invoice.domain.vo.InvoiceSnapshot;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class InvoiceIntegrationEventListener {

    @IntegrationEventTranslation
    public InvoiceIssuedIntegrationEvent translate(final InvoiceIssued event) {
        final InvoiceSnapshot snapshot = event.getSnapshot();
        return new InvoiceIssuedIntegrationEvent(snapshot.invoiceId(), snapshot.number(), snapshot.buyerEmail(),
                snapshot.buyerLanguage(), snapshot.documentId());
    }
}
