package com.warehouse.mail.configuration;

import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.application.port.primary.MailPortImpl;
import com.warehouse.mail.application.port.secondary.MailSenderServicePort;
import com.warehouse.mail.application.port.secondary.MailTemplateRepository;
import com.warehouse.mail.domain.service.MailTemplateRenderer;
import com.warehouse.mail.infrastructure.adapter.primary.MailListener;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.mail.infrastructure.adapter.primary.kafka.MailKafkaListener;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.MailLocaleResolver;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.ShipmentMailRequestMapper;
import com.warehouse.mail.infrastructure.adapter.secondary.DisabledMailServiceAdapter;
import com.warehouse.mail.infrastructure.adapter.secondary.MailTemplateReadRepository;
import com.warehouse.mail.infrastructure.adapter.secondary.MailTemplateRepositoryImpl;
import com.warehouse.mail.infrastructure.adapter.secondary.NotificationEventPublisherImpl;
import com.warehouse.mail.infrastructure.adapter.secondary.SmtpMailServiceAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MailConfiguration {

    @Bean
    @ConditionalOnProperty(name = "manager.mail.enabled", havingValue = "true")
    public MailSenderServicePort smtpMailSenderServicePort(final JavaMailSender javaMailSender,
                                                           @Value("${manager.mail.from.address}") final String fromAddress,
                                                           @Value("${manager.mail.from.name}") final String fromName) {
        return new SmtpMailServiceAdapter(javaMailSender, fromAddress, fromName);
    }

    @Bean
    @ConditionalOnProperty(name = "manager.mail.enabled", havingValue = "false", matchIfMissing = true)
    public MailSenderServicePort disabledMailSenderServicePort() {
        return new DisabledMailServiceAdapter();
    }

    @Bean
    public MailTemplateRepository mailTemplateRepository(final MailTemplateReadRepository mailTemplateReadRepository) {
        return new MailTemplateRepositoryImpl(mailTemplateReadRepository);
    }

    @Bean
    public MailLocaleResolver mailLocaleResolver(
            @Value("${manager.mail.default-locale}") final String defaultLocale,
            @Value("${manager.mail.unmapped-country-locale}") final String unmappedCountryLocale,
            @Value("${manager.mail.country-locales}") final String countryLocales) {
        final Map<String, Locale> locales = Arrays.stream(countryLocales.split(","))
                .map(String::trim)
                .filter(entry -> !entry.isEmpty())
                .map(entry -> entry.split(":"))
                .collect(Collectors.toMap(entry -> entry[0].trim().toUpperCase(Locale.ROOT),
                        entry -> Locale.forLanguageTag(entry[1].trim())));
        return new MailLocaleResolver(locales, Locale.forLanguageTag(defaultLocale),
                Locale.forLanguageTag(unmappedCountryLocale));
    }

    @Bean
    public MailPort mailPort(final MailTemplateRepository mailTemplateRepository,
                             final MailSenderServicePort mailSenderServicePort,
                             final MailLocaleResolver mailLocaleResolver,
                             @Value("${manager.mail.from.name}") final String fromName) {
        return new MailPortImpl(mailTemplateRepository, new MailTemplateRenderer(), mailSenderServicePort,
                mailLocaleResolver.defaultLocale(), Map.of("fromName", fromName));
    }

    @Bean
    public ShipmentMailRequestMapper shipmentMailRequestMapper(
            final MailLocaleResolver mailLocaleResolver,
            @Value("${manager.mail.links.tracking-url}") final String trackingUrl,
            @Value("${manager.mail.links.shipment-edit-url}") final String shipmentEditUrl) {
        return new ShipmentMailRequestMapper(mailLocaleResolver, trackingUrl, shipmentEditUrl);
    }

    @Bean
    public MailKafkaListener mailKafkaListener(final MailPort mailPort,
                                               final ShipmentMailRequestMapper shipmentMailRequestMapper) {
        return new MailKafkaListener(mailPort, shipmentMailRequestMapper);
    }

    @Bean
    public MailListener mailListener(final MailPort mailPort, final MailLocaleResolver mailLocaleResolver) {
        return new MailListener(mailPort, mailLocaleResolver);
    }

    @Bean
    public NotificationEventPublisher notificationEventPublisher(final ApplicationEventPublisher eventPublisher) {
        return new NotificationEventPublisherImpl(eventPublisher);
    }
}
