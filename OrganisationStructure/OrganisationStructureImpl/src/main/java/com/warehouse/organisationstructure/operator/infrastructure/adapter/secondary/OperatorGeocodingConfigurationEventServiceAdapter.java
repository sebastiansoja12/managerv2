package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.infrastructure.dto.GeocodingConfigurationCreateDto;
import com.warehouse.infrastructure.event.OperatorGeocodingConfigurationCreateEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorGeocodingConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorGeocodingConfiguration;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

public class OperatorGeocodingConfigurationEventServiceAdapter
        implements OperatorGeocodingConfigurationEventServicePort {

    private final ApplicationEventPublisher eventPublisher;

    public OperatorGeocodingConfigurationEventServiceAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp) {
        final OperatorGeocodingConfiguration configuration = snapshot.provisioningDetails().geocodingConfiguration();
        eventPublisher.publishEvent(new OperatorGeocodingConfigurationCreateEvent(
                snapshot.operatorId(),
                new GeocodingConfigurationCreateDto(
                        blankToNull(configuration.apiUserName()),
                        blankToNull(configuration.apiPassword()),
                        blankToNull(configuration.apiKey()),
                        blankToNull(configuration.clientNumber()),
                        blankToNull(configuration.accessToken()),
                        blankToNull(configuration.refreshToken()),
                        configuration.enabled(),
                        configuration.provider()
                ),
                timestamp
        ));
    }

    private String blankToNull(final String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
