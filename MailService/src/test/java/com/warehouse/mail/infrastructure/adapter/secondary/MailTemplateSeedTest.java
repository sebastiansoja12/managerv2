package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.domain.service.MailTemplateRenderer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.warehouse.mail.MailFixture.party;
import static com.warehouse.mail.MailFixture.shipment;
import static com.warehouse.mail.MailFixture.shipmentMailRequestMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailTemplateSeedTest {

    private static final Path SEED = Path.of(
            "../Application/src/main/resources/changelog/db/postgresql/changes/2026-10-18-mail-templates.xml");
    private static final Set<String> LOCALES = Set.of("pl", "en", "de");

    @Test
    void shouldProvideEveryTemplateInEveryLanguageAndRenderWithMapperVariables() throws Exception {
        final List<MailTemplate> templates = seededTemplates();
        final Map<String, Set<String>> localesByKey = new HashMap<>();
        templates.forEach(template -> localesByKey.computeIfAbsent(template.key(), key -> new HashSet<>())
                .add(template.locale().toLanguageTag()));
        final MailRequest variablesSource = shipmentMailRequestMapper().mapCreated(shipment(ShipmentStatus.CREATED,
                party("Jan", "jan@example.com", "PL"), party("Anna", "anna@example.com", "PL"))).get(0);
        final MailTemplateRenderer renderer = new MailTemplateRenderer();
        final Map<String, String> variables = new HashMap<>(variablesSource.variables());
        variables.put("fromName", "Manager");
        variables.put("code", "123456");
        variables.put("validMinutes", "15");

        assertEquals(17, localesByKey.size());
        localesByKey.forEach((key, locales) -> assertEquals(LOCALES, locales, key));
        templates.forEach(template -> {
            final MailMessage message = renderer.render(template, new MailRequest(template.key(), template.locale(),
                    variablesSource.recipient(), variables));
            assertTrue(message.subject().contains("PL 123/45"), template.key());
            assertFalse(message.body().contains("{{"), template.key());
            assertTrue(message.body().contains("Manager"), template.key());
        });
    }

    private static List<MailTemplate> seededTemplates() throws Exception {
        final NodeList inserts = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(SEED.toFile())
                .getElementsByTagName("insert");
        final List<MailTemplate> templates = new ArrayList<>();
        for (int index = 0; index < inserts.getLength(); index++) {
            final NodeList columns = ((Element) inserts.item(index)).getElementsByTagName("column");
            final Map<String, String> values = new HashMap<>();
            for (int column = 0; column < columns.getLength(); column++) {
                final Element element = (Element) columns.item(column);
                values.put(element.getAttribute("name"), element.hasAttribute("value")
                        ? element.getAttribute("value") : element.getTextContent());
            }
            templates.add(new MailTemplate(values.get("template_key"), Locale.forLanguageTag(values.get("locale")),
                    values.get("subject"), values.get("body")));
        }
        return templates;
    }
}
