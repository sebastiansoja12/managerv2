package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.model.SoftwareProperty;
import com.warehouse.shipment.domain.model.SoftwareConfigurationProperties;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.SoftwareConfigurationDto;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SoftwareConfigurationServiceAdapter implements SoftwareConfigurationServicePort {

    private final Retry retry;

    public SoftwareConfigurationServiceAdapter(final RetryConfig retryConfig) {
        this.retry = Retry.of("softwareConfiguration", retryConfig);
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
    public SoftwareConfiguration getSoftwareConfiguration(final SoftwareConfigurationProperties properties) {

        final RestClient restClient = RestClient
                .builder()
                .baseUrl(properties.getUrl())
                .build();

        final SoftwareProperty softwareProperty = new SoftwareProperty(properties.getEndpoint(), "route-tracker-initialize-url",
                "");

        final Supplier<ResponseEntity<SoftwareConfigurationDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> getSoftwareConfiguration(restClient, softwareProperty));

        final ResponseEntity<SoftwareConfigurationDto> process = retryableSupplier.get();

        if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while retrieving configuration");
            return null;
        }

        return SoftwareConfiguration.from(process.getBody());
    }
}
