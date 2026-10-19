package com.warehouse.mail;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mail.application.port.secondary.MailSenderServicePort;
import com.warehouse.mail.application.port.secondary.MailTemplateRepository;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailTemplate;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.MailLocaleResolver;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.ShipmentMailRequestMapper;
import com.warehouse.shipment.api.event.snapshot.PartySnapshot;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class MailFixture {

    public static final Locale POLISH = Locale.forLanguageTag("pl");
    public static final Locale GERMAN = Locale.forLanguageTag("de");
    public static final Locale ENGLISH = Locale.forLanguageTag("en");
    public static final String TRACKING_URL = "https://manager.example/tracking";
    public static final String SHIPMENT_EDIT_URL = "https://manager.example/shipment-edit";

    private MailFixture() {
    }

    public static MailLocaleResolver localeResolver() {
        return new MailLocaleResolver(Map.of("PL", POLISH, "DE", GERMAN, "AT", GERMAN), POLISH, ENGLISH);
    }

    public static ShipmentMailRequestMapper shipmentMailRequestMapper() {
        return new ShipmentMailRequestMapper(localeResolver(), TRACKING_URL, SHIPMENT_EDIT_URL);
    }

    public static PartySnapshot party(final String firstName, final String email, final String countryCode) {
        return new PartySnapshot(firstName, "Nowak", email, "500600700", "Katowice", "40-001", "Mariacka 1",
                countryCode);
    }

    public static ShipmentEventData shipment(final ShipmentStatus status,
                                             final PartySnapshot sender,
                                             final PartySnapshot recipient) {
        return shipment(status, sender, recipient, null);
    }

    public static ShipmentEventData shipment(final ShipmentStatus status,
                                             final PartySnapshot sender,
                                             final PartySnapshot recipient,
                                             final ShipmentId relatedShipmentId) {
        return new ShipmentEventData(new ShipmentId(100L), sender, recipient, status, null,
                relatedShipmentId, null, null, null, false, false, null, new TrackingNumber("PL 123/45"), null,
                null, null);
    }

    public static final class InMemoryMailTemplateRepository implements MailTemplateRepository {

        private final Map<String, MailTemplate> templates = new HashMap<>();

        public InMemoryMailTemplateRepository with(final MailTemplate template) {
            this.templates.put(template.key() + "|" + template.locale().toLanguageTag(), template);
            return this;
        }

        @Override
        public Optional<MailTemplate> find(final String templateKey, final Locale locale) {
            return Optional.ofNullable(this.templates.get(templateKey + "|" + locale.toLanguageTag()));
        }
    }

    public static final class RecordingMailSender implements MailSenderServicePort {

        private final List<MailMessage> sent = new ArrayList<>();

        @Override
        public void send(final MailMessage mailMessage) {
            this.sent.add(mailMessage);
        }

        public List<MailMessage> sent() {
            return this.sent;
        }
    }
}
