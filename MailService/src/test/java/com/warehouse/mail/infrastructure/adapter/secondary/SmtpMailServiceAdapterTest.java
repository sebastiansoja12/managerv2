package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.mail.infrastructure.adapter.secondary.exception.WarehouseMailException;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SmtpMailServiceAdapterTest {

    private final MailMessage mailMessage = new MailMessage(new EmailAddress("anna@example.com"),
            "Przesyłka PL1 została doręczona", "Dzień dobry Anna,\n\nżółć");

    @Test
    void shouldSendUtf8PlainTextMessageFromConfiguredAddress() throws Exception {
        final RecordingJavaMailSender javaMailSender = new RecordingJavaMailSender(false);
        final SmtpMailServiceAdapter adapter = new SmtpMailServiceAdapter(javaMailSender, "no-reply@manager.pl",
                "Manager");

        adapter.send(mailMessage);

        final MimeMessage mimeMessage = javaMailSender.sent().get(0);
        assertEquals("Przesyłka PL1 została doręczona", mimeMessage.getSubject());
        assertEquals("anna@example.com", mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());
        assertEquals("no-reply@manager.pl", ((InternetAddress) mimeMessage.getFrom()[0]).getAddress());
        assertEquals("Manager", ((InternetAddress) mimeMessage.getFrom()[0]).getPersonal());
        assertEquals("Dzień dobry Anna,\n\nżółć", mimeMessage.getContent());
    }

    @Test
    void shouldThrowExceptionWhenSmtpServerRejectsMessage() {
        final SmtpMailServiceAdapter adapter = new SmtpMailServiceAdapter(new RecordingJavaMailSender(true),
                "no-reply@manager.pl", "Manager");

        final WarehouseMailException exception = assertThrows(WarehouseMailException.class,
                () -> adapter.send(mailMessage));

        assertEquals("E-mail was not sent because of: Connection refused", exception.getMessage());
    }

    private static final class RecordingJavaMailSender extends JavaMailSenderImpl {

        private final boolean failing;
        private final List<MimeMessage> sent = new ArrayList<>();

        private RecordingJavaMailSender(final boolean failing) {
            this.failing = failing;
        }

        @Override
        protected void doSend(final MimeMessage[] mimeMessages, final Object[] originalMessages) {
            if (this.failing) {
                throw new MailSendException("Connection refused");
            }
            this.sent.addAll(Arrays.asList(mimeMessages));
        }

        private List<MimeMessage> sent() {
            return this.sent;
        }
    }
}
