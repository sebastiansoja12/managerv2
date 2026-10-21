package com.warehouse.driverhub.application.port.primary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.driverhub.application.port.primary.command.DriverCommand;
import com.warehouse.driverhub.application.port.secondary.CarrierRepository;
import com.warehouse.driverhub.application.port.secondary.CurrentOperatorServicePort;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.model.CarrierDriver;
import com.warehouse.driverhub.domain.vo.DriverChange;
import com.warehouse.driverhub.domain.vo.DriverDetails;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DriverPortImpl implements DriverPort {

    private final CarrierRepository carrierRepository;
    private final CurrentOperatorServicePort currentOperatorServicePort;

    public DriverPortImpl(final CarrierRepository carrierRepository,
                          final CurrentOperatorServicePort currentOperatorServicePort) {
        this.carrierRepository = carrierRepository;
        this.currentOperatorServicePort = currentOperatorServicePort;
    }

    @Override
    public List<DriverDetails> findDrivers(final boolean activeOnly) {
        return this.carrierRepository.findAll(operatorId(), false).stream()
                .flatMap(carrier -> carrier.getDrivers().stream()
                        .filter(driver -> !activeOnly || driver.isActive())
                        .map(driver -> details(carrier, driver)))
                .sorted(Comparator.comparing((DriverDetails details) -> details.driver().getLastName())
                        .thenComparing(details -> details.driver().getFirstName()))
                .toList();
    }

    @Override
    public DriverDetails getDriver(final DriverId driverId) {
        final Carrier carrier = carrierOf(driverId);
        return details(carrier, carrier.driver(driverId));
    }

    @Override
    public DriverDetails addDriver(final CarrierId carrierId, final DriverCommand command) {
        if (carrierId == null || carrierId.getValue() == null) {
            throw DriverHubException.invalid("Carrier is required");
        }
        if (command == null) {
            throw DriverHubException.invalid("Request body is required");
        }
        final Carrier carrier = this.carrierRepository.findById(carrierId, operatorId())
                .orElseThrow(() -> DriverHubException.notFound("Carrier " + carrierId.getValue() + " not found"));
        final CarrierDriver added = carrier.addDriver(command.firstName(), command.lastName(), command.phone(),
                command.licenseNumber());
        final Carrier saved = this.carrierRepository.save(carrier);
        return details(saved, saved.driver(added.getDriverId()));
    }

    @Override
    public DriverDetails changeDriver(final DriverId driverId, final DriverChange change) {
        final Carrier carrier = carrierOf(driverId);
        carrier.changeDriver(driverId, change);
        final Carrier saved = this.carrierRepository.save(carrier);
        return details(saved, saved.driver(driverId));
    }

    private Carrier carrierOf(final DriverId driverId) {
        if (driverId == null || driverId.getValue() == null) {
            throw DriverHubException.invalid("Driver id is required");
        }
        return this.carrierRepository.findByDriverId(driverId, operatorId())
                .orElseThrow(() -> DriverHubException.notFound("Driver " + driverId.getValue() + " not found"));
    }

    private OperatorId operatorId() {
        return this.currentOperatorServicePort.currentOperatorId();
    }

    private static DriverDetails details(final Carrier carrier, final CarrierDriver driver) {
        Objects.requireNonNull(driver, "driver");
        return new DriverDetails(carrier.getCarrierId(), carrier.getName(), carrier.isActive(), driver);
    }
}
