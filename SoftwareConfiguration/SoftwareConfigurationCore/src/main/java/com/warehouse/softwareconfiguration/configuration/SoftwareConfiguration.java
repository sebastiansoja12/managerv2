package com.warehouse.softwareconfiguration.configuration;

import com.warehouse.softwareconfiguration.domain.port.primary.SoftwareConfigurationPort;
import com.warehouse.softwareconfiguration.domain.port.primary.SoftwareConfigurationPortImpl;
import com.warehouse.softwareconfiguration.domain.port.secondary.SoftwareConfigurationRepository;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.SoftwareConfigurationDocumentReadRepository;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.SoftwareConfigurationRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoftwareConfiguration {
    
    @Bean
	public SoftwareConfigurationPort softwareConfigurationPort(
			SoftwareConfigurationRepository softwareConfigurationRepository) {
        return new SoftwareConfigurationPortImpl(softwareConfigurationRepository);
    }
    
    @Bean
	public SoftwareConfigurationRepository softwareConfigurationRepository(
			SoftwareConfigurationDocumentReadRepository softwareConfigurationDocumentReadRepository) {
        return new SoftwareConfigurationRepositoryImpl(softwareConfigurationDocumentReadRepository);
    }
}
