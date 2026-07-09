package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.model.SoftwareProperty;
import com.warehouse.terminal.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.terminal.domain.vo.DeviceValidation;
import com.warehouse.terminal.infrastructure.adapter.secondary.api.SoftwareConfigurationDto;
import com.warehouse.tools.softwareconfiguration.SoftwareConfigurationProperties;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.function.Supplier;

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
                .onStatus(HttpStatusCode::isError, (request, response) -> new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                .toEntity(SoftwareConfigurationDto.class);
    }

    @Override
    public DeviceValidation getSoftwareDeviceValidation(final DeviceId deviceId) {

        final RestClient restClient = RestClient
                .builder()
                .baseUrl(softwareConfigurationProperties.getUrl())
                .build();

        final SoftwareProperty softwareProperty = new SoftwareProperty(softwareConfigurationProperties.getEndpoint(),
                deviceId.value().toString(),"");

        final Supplier<ResponseEntity<SoftwareConfigurationDto>> retryableSupplier = Retry
                .decorateSupplier(retry, () -> getSoftwareConfiguration(restClient, softwareProperty));

        final ResponseEntity<SoftwareConfigurationDto> process = retryableSupplier.get();

        if (!process.getStatusCode().is2xxSuccessful() || process.getBody() == null) {
            log.error("Error while retrieving configuration, cause: {}, will use default configuration", process.getStatusCode());
            return new DeviceValidation(deviceId, true);
        }

        return DeviceValidation.from(process.getBody());
    }
}
