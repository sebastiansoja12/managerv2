package com.warehouse.analytics.configuration;

import com.warehouse.commonassets.identificator.DepartmentCostId;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class OrganizationAnalyticsIdentifierWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, DepartmentCostId.class, source -> new DepartmentCostId(Long.valueOf(source)));
    }
}
