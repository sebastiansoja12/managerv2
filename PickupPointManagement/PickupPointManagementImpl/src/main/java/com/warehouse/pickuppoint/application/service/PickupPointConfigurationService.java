package com.warehouse.pickuppoint.application.service;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.port.primary.command.PickupPointConfigurationCommand;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointCoordinatesServicePort;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;

import java.time.Instant;
import java.util.Objects;

public class PickupPointConfigurationService {

    private final PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort;

    public PickupPointConfigurationService(final PickupPointCoordinatesServicePort pickupPointCoordinatesServicePort) {
        this.pickupPointCoordinatesServicePort = pickupPointCoordinatesServicePort;
    }

    public PickupPoint create(
            final PickupPointCode code,
            final PickupPointConfigurationCommand configuration,
            final Instant timestamp) {
        return PickupPoint.create(
                PickupPointId.generate(),
                code,
                configuration.name(),
                configuration.type(),
                configuration.capabilities(),
                configuration.address(),
                this.pickupPointCoordinatesServicePort.getCoordinates(configuration.address()),
                configuration.departmentId(),
                configuration.contact(),
                configuration.accessInstructions(),
                configuration.openingSchedule(),
                configuration.servicePolicy(),
                configuration.externalReference(),
                timestamp);
    }

    public void configure(
            final PickupPoint pickupPoint,
            final PickupPointConfigurationCommand configuration,
            final Instant timestamp) {
        pickupPoint.changeType(configuration.type(), timestamp);
        pickupPoint.changeCapabilities(configuration.capabilities(), timestamp);
        pickupPoint.updateDetails(
                configuration.name(),
                configuration.address(),
                coordinates(pickupPoint, configuration),
                configuration.departmentId(),
                configuration.contact(),
                configuration.accessInstructions(),
                configuration.externalReference(),
                timestamp);
        pickupPoint.changeOpeningSchedule(configuration.openingSchedule(), timestamp);
        pickupPoint.changeServicePolicy(configuration.servicePolicy(), timestamp);
    }

    private GeoCoordinates coordinates(
            final PickupPoint pickupPoint,
            final PickupPointConfigurationCommand configuration) {
        if (Objects.equals(pickupPoint.snapshot().address(), configuration.address())) {
            return pickupPoint.snapshot().coordinates();
        }
        return this.pickupPointCoordinatesServicePort.getCoordinates(configuration.address());
    }
}
