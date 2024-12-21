package com.warehouse.mail.configuration;

import com.warehouse.mail.domain.port.secondary.MailServicePort;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.mail.domain.service.MailServiceImpl;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.mail.infrastructure.adapter.secondary.MailServiceCreatorAdapter;
import com.warehouse.mail.infrastructure.adapter.secondary.NotificationEventPublisherImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfiguration {


    @Bean
    public MailService mailService(MailServicePort mailServicePort) {
        return new MailServiceImpl(mailServicePort);
    }

    @Bean
    public MailServicePort mailServicePort() {
        return new MailServiceCreatorAdapter();
    }

    @Bean
    public NotificationEventPublisher notificationEventPublisher(final ApplicationEventPublisher eventPublisher) {
        return new NotificationEventPublisherImpl(eventPublisher);
    }

    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}
