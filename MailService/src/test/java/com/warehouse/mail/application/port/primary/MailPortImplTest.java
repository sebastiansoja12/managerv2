package com.warehouse.mail.application.port.primary;

import com.warehouse.mail.MailFixture.InMemoryMailTemplateRepository;
import com.warehouse.mail.MailFixture.RecordingMailSender;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.domain.service.MailTemplateRenderer;
import com.warehouse.mail.domain.vo.EmailAddress;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.warehouse.mail.MailFixture.ENGLISH;
import static com.warehouse.mail.MailFixture.GERMAN;
import static com.warehouse.mail.MailFixture.POLISH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailPortImplTest {

    private final InMemoryMailTemplateRepository templates = new InMemoryMailTemplateRepository()
            .with(new MailTemplate("shipment.created.sender", POLISH, "Nadano {{trackingNumber}}", "Treść PL\n{{fromName}}"))
            .with(new MailTemplate("shipment.created.sender", GERMAN, "Aufgegeben {{trackingNumber}}", "Inhalt DE\n{{fromName}}"));
    private final RecordingMailSender sender = new RecordingMailSender();
    private final MailPortImpl mailPort = new MailPortImpl(templates, new MailTemplateRenderer(), sender, POLISH,
            Map.of("fromName", "Manager"));

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

    private static MailRequest request(final String templateKey, final Locale locale) {
        return new MailRequest(templateKey, locale, new EmailAddress("jan@example.com"),
                Map.of("trackingNumber", "PL1"));
    }
}
