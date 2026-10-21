package com.warehouse.mail.domain.service;

import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.domain.vo.EmailAddress;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.warehouse.mail.MailFixture.POLISH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MailTemplateRendererTest {

    private final MailTemplateRenderer renderer = new MailTemplateRenderer();

    @Test
    void shouldReplaceVariablesAndRenderOptionalSectionsWhenValuePresent() {
        final MailTemplate template = new MailTemplate("test", POLISH, "Przesyłka {{trackingNumber}}",
                "Dzień dobry{{#firstName}} {{firstName}}{{/firstName}},\n\n{{#trackingUrl}}Link: {{trackingUrl}}{{/trackingUrl}}");

        final MailMessage message = renderer.render(template, request(Map.of("trackingNumber", "PL1",
                "firstName", "Anna", "trackingUrl", "https://t/PL1")));

        assertEquals("Przesyłka PL1", message.subject());
        assertEquals("Dzień dobry Anna,\n\nLink: https://t/PL1", message.body());
        assertEquals("anna@example.com", message.recipient().value());
    }

    @Test
    void shouldSkipOptionalSectionsWhenValueIsBlank() {
        final MailTemplate template = new MailTemplate("test", POLISH, "Temat",
                "Dzień dobry{{#firstName}} {{firstName}}{{/firstName}},\n\n\n\n{{#trackingUrl}}Link: {{trackingUrl}}{{/trackingUrl}}Koniec");

        final MailMessage message = renderer.render(template, request(Map.of("firstName", "", "trackingUrl", "")));

        assertEquals("Dzień dobry,\n\nKoniec", message.body());
    }

    @Test
    void shouldThrowExceptionWhenTemplateUsesMissingVariable() {
        final MailTemplate template = new MailTemplate("test", POLISH, "Przesyłka {{trackingNumber}}", "Treść");

        final MailException exception = assertThrows(MailException.class,
                () -> renderer.render(template, request(Map.of())));

        assertEquals("Mail template test requires variable trackingNumber", exception.getMessage());
    }

    @Test
    void shouldNotInterpretRegexCharactersInValues() {
        final MailTemplate template = new MailTemplate("test", POLISH, "Temat", "Kwota {{amount}}");

        final MailMessage message = renderer.render(template, request(Map.of("amount", "$1\\2")));

        assertEquals("Kwota $1\\2", message.body());
    }

    private static MailRequest request(final Map<String, String> variables) {
        return new MailRequest("test", POLISH, new EmailAddress("anna@example.com"), variables);
    }
}
