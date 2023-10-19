package com.warehouse.documentation.configuration;

import com.warehouse.documentation.domain.port.primary.DocumentationPort;
import com.warehouse.documentation.domain.port.primary.DocumentationPortImpl;
import com.warehouse.documentation.domain.service.DocumentationService;
import com.warehouse.documentation.domain.service.DocumentationServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfiguration {


    @Bean
    public DocumentationPort documentationPort() {
        final DocumentationService documentationService = new DocumentationServiceImpl();
        return new DocumentationPortImpl(documentationService);
    }
}
