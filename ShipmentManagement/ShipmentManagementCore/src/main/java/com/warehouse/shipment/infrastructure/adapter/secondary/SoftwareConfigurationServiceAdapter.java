package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.function.Supplier;

import com.warehouse.commonassets.enumeration.SoftwareConfigurationUrl;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.model.SoftwareProperty;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.SoftwareConfigurationDto;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SoftwareConfigurationServiceAdapter implements SoftwareConfigurationServicePort {

    private final Retry retry;
    
    private final SoftwareConfigurationProperties softwareConfigurationProperties;

	public SoftwareConfigurationServiceAdapter(final RetryConfig retryConfig,
			final SoftwareConfigurationProperties softwareConfigurationProperties) {
        this.retry = Retry.of("softwareConfiguration", retryConfig);
        this.softwareConfigurationProperties = softwareConfigurationProperties;
    }
    
	private ResponseEntity<SoftwareConfigurationDto> getSoftwareConfiguration(final RestClient restClient,
			final SoftwareProperty property) {
        return restClient
                .get()
                .uri(property.getCategory() + "/" + property.getName())
                .retrieve()
                .toEntity(SoftwareConfigurationDto.class);
	}

    @Override
    public SoftwareConfiguration getSoftwareConfiguration() {

        final RestClient restClient = RestClient
                .builder()
                .baseUrl(softwareConfigurationProperties.getUrl())
                .build();

		final SoftwareProperty softwareProperty = new SoftwareProperty(softwareConfigurationProperties.getEndpoint(),
                SoftwareConfigurationUrl.ROUTE_TRACKER_URL.getUrl(),"");

        final Supplier<ResponseEntity<SoftwareConfigurationDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> getSoftwareConfiguration(restClient, softwareProperty));

        final ResponseEntity<SoftwareConfigurationDto> process = retryableSupplier.get();

        if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while retrieving configuration, cause: {}", process.getStatusCode());
            throw new RuntimeException("Error while retrieving configuration");
        }

        return SoftwareConfiguration.from(process.getBody());
    }
}
