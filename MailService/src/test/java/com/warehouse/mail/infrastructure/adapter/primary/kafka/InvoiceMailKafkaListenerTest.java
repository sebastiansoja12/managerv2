package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.billing.api.event.InvoiceIssuedIntegrationEvent;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.MailAttachmentReference;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.InvoiceMailRequestMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.warehouse.mail.MailFixture.GERMAN;
import static com.warehouse.mail.MailFixture.POLISH;
import static com.warehouse.mail.MailFixture.localeResolver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvoiceMailKafkaListenerTest {

    private static final UUID DOCUMENT_ID = UUID.fromString("7f6a3a9c-1d7e-4c55-9f1e-2a3b4c5d6e7f");

    private final RecordingMailPort mailPort = new RecordingMailPort();

    private final InvoiceMailKafkaListener listener = new InvoiceMailKafkaListener(this.mailPort,
            new InvoiceMailRequestMapper(localeResolver()));

    @Test
    void shouldSendIssuedInvoiceWithPdfAttachment() {
        this.listener.handle(new InvoiceIssuedIntegrationEvent(new InvoiceId(9L), "FV/2026/11/0001",
                "jan@example.com", "de", DOCUMENT_ID));

        assertEquals(1, this.mailPort.requests.size());
        final MailRequest request = this.mailPort.requests.get(0);
        assertEquals("invoice.issued", request.templateKey());
        assertEquals(GERMAN, request.locale());
        assertEquals("jan@example.com", request.recipient().value());
        assertEquals(Map.of("invoiceNumber", "FV/2026/11/0001"), request.variables());
        assertEquals(List.of(new MailAttachmentReference(DOCUMENT_ID)), request.attachments());
    }

    @Test
    void shouldUseDefaultLocaleWhenBuyerLanguageIsUnknown() {
        this.listener.handle(new InvoiceIssuedIntegrationEvent(new InvoiceId(9L), "FV/2026/11/0001",
                "jan@example.com", null, DOCUMENT_ID));

        assertEquals(POLISH, this.mailPort.requests.get(0).locale());
    }

    @Test
    void shouldSkipMailWhenBuyerEmailIsUnknown() {
        this.listener.handle(new InvoiceIssuedIntegrationEvent(new InvoiceId(9L), "FV/2026/11/0001", null, "pl",
                DOCUMENT_ID));

        assertTrue(this.mailPort.requests.isEmpty());
    }

    private static final class RecordingMailPort implements MailPort {

        private final List<MailRequest> requests = new ArrayList<>();

        @Override
        public void send(final List<MailRequest> mailRequests) {
            this.requests.addAll(mailRequests);
        }

        @Override
        public void send(final MailMessage mailMessage) {
        }
    }
}
