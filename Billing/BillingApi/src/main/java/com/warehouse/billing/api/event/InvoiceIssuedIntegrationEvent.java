package com.warehouse.billing.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "billing.invoice-issued", version = 1, topic = BillingTopics.INVOICE_ISSUED)
public class InvoiceIssuedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final InvoiceId invoiceId;

    private final String number;

    private final String buyerEmail;

    private final String buyerLanguage;

    private final UUID documentId;

    @JsonCreator
    public InvoiceIssuedIntegrationEvent(@JsonProperty("invoiceId") final InvoiceId invoiceId,
                                         @JsonProperty("number") final String number,
                                         @JsonProperty("buyerEmail") final String buyerEmail,
                                         @JsonProperty("buyerLanguage") final String buyerLanguage,
                                         @JsonProperty("documentId") final UUID documentId) {
        this.invoiceId = invoiceId;
        this.number = number;
        this.buyerEmail = buyerEmail;
        this.buyerLanguage = buyerLanguage;
        this.documentId = documentId;
    }

    @JsonProperty("invoiceId")
    public InvoiceId invoiceId() {
        return invoiceId;
    }

    @JsonProperty("number")
    public String number() {
        return number;
    }

    @JsonProperty("buyerEmail")
    public String buyerEmail() {
        return buyerEmail;
    }

    @JsonProperty("buyerLanguage")
    public String buyerLanguage() {
        return buyerLanguage;
    }

    @JsonProperty("documentId")
    public UUID documentId() {
        return documentId;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.invoiceId.value());
    }
}
