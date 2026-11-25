package com.warehouse.mail.application.port.primary;

import com.warehouse.mail.MailFixture.InMemoryMailTemplateRepository;
import com.warehouse.mail.MailFixture.RecordingMailSender;
import com.warehouse.mail.application.service.MailAttachmentResolver;
import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.domain.service.MailTemplateRenderer;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.mail.domain.vo.MailAttachment;
import com.warehouse.mail.domain.vo.MailAttachmentReference;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.warehouse.mail.MailFixture.ENGLISH;
import static com.warehouse.mail.MailFixture.GERMAN;
import static com.warehouse.mail.MailFixture.POLISH;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailPortImplTest {

    private final InMemoryMailTemplateRepository templates = new InMemoryMailTemplateRepository()
            .with(new MailTemplate("shipment.created.sender", POLISH, "Nadano {{trackingNumber}}", "Treść PL\n{{fromName}}"))
            .with(new MailTemplate("shipment.created.sender", GERMAN, "Aufgegeben {{trackingNumber}}", "Inhalt DE\n{{fromName}}"))
            .with(new MailTemplate("invoice.issued", POLISH, "Faktura {{invoiceNumber}}", "W załączniku {{invoiceNumber}}"));
    private static final UUID DOCUMENT_ID = UUID.fromString("7f6a3a9c-1d7e-4c55-9f1e-2a3b4c5d6e7f");
    private static final byte[] PDF = {0x25, 0x50, 0x44, 0x46};
    private final RecordingMailSender sender = new RecordingMailSender();
    private final MailPortImpl mailPort = new MailPortImpl(templates, new MailTemplateRenderer(), sender, POLISH,
            Map.of("fromName", "Manager"), new MailAttachmentResolver(reference -> DOCUMENT_ID.equals(
                    reference.documentId())
                    ? Optional.of(new MailAttachment("FV_2026_11_0001.pdf", "application/pdf", PDF))
                    : Optional.empty()));

    @Test
    void shouldRenderTemplateInRequestedLanguageWithGlobalVariables() {
        mailPort.send(List.of(request("shipment.created.sender", GERMAN)));

        assertEquals(1, sender.sent().size());
        assertEquals("Aufgegeben PL1", sender.sent().get(0).subject());
        assertEquals("Inhalt DE\nManager", sender.sent().get(0).body());
    }

    @Test
    void shouldFallBackToLanguageWhenRegionalTemplateIsMissing() {
        mailPort.send(List.of(request("shipment.created.sender", Locale.forLanguageTag("de-AT"))));

        assertEquals("Aufgegeben PL1", sender.sent().get(0).subject());
    }

    @Test
    void shouldFallBackToDefaultLocaleWhenLanguageTemplateIsMissing() {
        mailPort.send(List.of(request("shipment.created.sender", ENGLISH)));

        assertEquals("Nadano PL1", sender.sent().get(0).subject());
    }

    @Test
    void shouldSkipRequestWhenTemplateDoesNotExist() {
        mailPort.send(List.of(request("shipment.status.prepared.recipient", POLISH),
                request("shipment.created.sender", POLISH)));

        assertEquals(1, sender.sent().size());
        assertEquals("Nadano PL1", sender.sent().get(0).subject());
    }

    @Test
    void shouldSendNothingWhenNoRequests() {
        mailPort.send(List.of());

        assertTrue(sender.sent().isEmpty());
    }

    @Test
    void shouldAttachReferencedDocumentToRenderedMail() {
        mailPort.send(List.of(invoiceRequest(DOCUMENT_ID)));

        assertEquals("Faktura FV/2026/11/0001", sender.sent().get(0).subject());
        assertEquals(1, sender.sent().get(0).attachments().size());
        final MailAttachment attachment = sender.sent().get(0).attachments().get(0);
        assertEquals("FV_2026_11_0001.pdf", attachment.fileName());
        assertEquals("application/pdf", attachment.contentType());
        assertArrayEquals(PDF, attachment.content());
    }

    @Test
    void shouldFailWhenReferencedDocumentIsMissing() {
        assertThrows(MailException.class, () -> mailPort.send(List.of(invoiceRequest(UUID.randomUUID()))));

        assertTrue(sender.sent().isEmpty());
    }

    private static MailRequest invoiceRequest(final UUID documentId) {
        return new MailRequest("invoice.issued", POLISH, new EmailAddress("jan@example.com"),
                Map.of("invoiceNumber", "FV/2026/11/0001"), List.of(new MailAttachmentReference(documentId)));
    }

    private static MailRequest request(final String templateKey, final Locale locale) {
        return new MailRequest(templateKey, locale, new EmailAddress("jan@example.com"),
                Map.of("trackingNumber", "PL1"));
    }
}
