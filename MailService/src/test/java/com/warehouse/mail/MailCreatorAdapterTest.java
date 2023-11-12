package com.warehouse.mail;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ContextConfiguration;

import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.mail.infrastructure.adapter.secondary.MailServiceCreatorAdapter;
import com.warehouse.mail.infrastructure.adapter.secondary.exception.WarehouseMailException;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = MailTestConfiguration.class)
public class MailCreatorAdapterTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailServiceCreatorAdapter mailServiceCreatorAdapter;


    @Test
    public void shouldSendNotification() {
        // given
        final Notification notification = Notification.builder()
                .body("Test Body")
                .recipient("test@example.com")
                .subject("Test Subject")
                .build();

        // when
        mailServiceCreatorAdapter.sendNotification(notification);

        // then
        verify(mailSender, times(1)).send(any(MimeMessagePreparator.class));
    }

    @Test
    public void shouldFail() {
        // given
        final Notification notification = Notification.builder()
                .body("Test Body")
                .recipient("test@example.com")
                .subject("Test Subject")
                .build();

		doThrow(new MailException("Simulated mail sending failure") {
		}).when(mailSender).send(any(MimeMessagePreparator.class));

        // when
        final Executable executable = () -> mailServiceCreatorAdapter.sendNotification(notification);

        // then
        final WarehouseMailException mailException = assertThrows(WarehouseMailException.class, executable);
        assertEquals("E-mail was not sent because of: Simulated mail sending failure",
                mailException.getMessage());
    }

}
