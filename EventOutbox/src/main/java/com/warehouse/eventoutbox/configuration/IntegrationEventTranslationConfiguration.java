package com.warehouse.eventoutbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.eventoutbox.publication.IntegrationEventTranslationRegistry;

@Configuration
public class IntegrationEventTranslationConfiguration {

    @Bean
    public static IntegrationEventTranslationRegistry integrationEventTranslationRegistry() {
        return new IntegrationEventTranslationRegistry();
    }
}
