package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.SupplierMailRequestMapper;
import com.warehouse.mail.api.event.SupplierDriverLicenseInvalidatedIntegrationEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.warehouse.mail.MailFixture.POLISH;
import static com.warehouse.mail.MailFixture.localeResolver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierMailKafkaListenerTest {

    private final RecordingMailPort mailPort = new RecordingMailPort();
    private final SupplierMailKafkaListener listener = new SupplierMailKafkaListener(mailPort,
            new SupplierMailRequestMapper(localeResolver()));

    @Test
    void shouldSendDriverLicenseInvalidatedMail() {
        listener.handle(new SupplierDriverLicenseInvalidatedIntegrationEvent(new SupplierCode("ABC123"),
                "jan@example.com", "Jan", Instant.now()));

        assertEquals(1, mailPort.requests.size());
        final MailRequest request = mailPort.requests.get(0);
        assertEquals("supplier.driver-license.invalidated", request.templateKey());
        assertEquals(POLISH, request.locale());
        assertEquals(Map.of("firstName", "Jan", "supplierCode", "ABC123"), request.variables());
    }

    @Test
    void shouldSkipMailWhenEmailIsInvalid() {
        listener.handle(new SupplierDriverLicenseInvalidatedIntegrationEvent(new SupplierCode("ABC123"),
                "not-an-email", "Jan", Instant.now()));

        assertTrue(mailPort.requests.isEmpty());
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
