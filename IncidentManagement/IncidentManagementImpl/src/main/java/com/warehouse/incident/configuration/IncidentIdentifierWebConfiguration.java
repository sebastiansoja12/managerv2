package com.warehouse.incident.configuration;

import com.warehouse.commonassets.identificator.IncidentId;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class IncidentIdentifierWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, IncidentId.class, source -> new IncidentId(Long.valueOf(source)));
    }
}
