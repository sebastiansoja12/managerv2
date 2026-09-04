package com.warehouse.mail.infrastructure.adapter.primary;

import com.warehouse.mail.MailFixture.InMemoryMailTemplateRepository;
import com.warehouse.mail.MailFixture.RecordingMailSender;
import com.warehouse.mail.application.port.primary.MailPortImpl;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.domain.service.MailTemplateRenderer;
import com.warehouse.mail.infrastructure.adapter.primary.event.MailTemplateNotificationEvent;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.warehouse.mail.MailFixture.GERMAN;
import static com.warehouse.mail.MailFixture.POLISH;
import static com.warehouse.mail.MailFixture.localeResolver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailListenerTest {

    private final RecordingMailSender sender = new RecordingMailSender();
    private final MailListener listener = new MailListener(new MailPortImpl(new InMemoryMailTemplateRepository()
            .with(new MailTemplate("shipment.reroute.token", POLISH, "Kod {{trackingNumber}}", "Kod: {{code}}"))
            .with(new MailTemplate("shipment.reroute.token", GERMAN, "Code {{trackingNumber}}", "Code: {{code}}")),
            new MailTemplateRenderer(), sender, POLISH, Map.of()), localeResolver());

    @Test
    void shouldSendTemplatedNotificationInRecipientCountryLanguage() {
        listener.handle(new MailTemplateNotificationEvent("shipment.reroute.token", "hans@example.de", "DE",
                Map.of("trackingNumber", "PL1", "code", "123456")));

        assertEquals(1, sender.sent().size());
        assertEquals("Code PL1", sender.sent().get(0).subject());
        assertEquals("Code: 123456", sender.sent().get(0).body());
        assertEquals("hans@example.de", sender.sent().get(0).recipient().value());
    }

    @Test
    void shouldSkipTemplatedNotificationWithInvalidRecipient() {
        listener.handle(new MailTemplateNotificationEvent("shipment.reroute.token", "invalid", "PL", Map.of()));

        assertTrue(sender.sent().isEmpty());
    }
}
