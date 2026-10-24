package com.warehouse.driverhub.application.port.primary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.application.port.primary.command.CarrierCommand;
import com.warehouse.driverhub.application.port.primary.command.VehicleCommand;
import com.warehouse.driverhub.application.port.secondary.CarrierRepository;
import com.warehouse.driverhub.application.port.secondary.CurrentOperatorServicePort;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.vo.CarrierChange;
import com.warehouse.driverhub.domain.vo.VehicleChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CarrierPortImpl implements CarrierPort {

    private static final Logger log = LoggerFactory.getLogger(CarrierPortImpl.class);

    private final CarrierRepository carrierRepository;
    private final CurrentOperatorServicePort currentOperatorServicePort;

    public CarrierPortImpl(final CarrierRepository carrierRepository,
                           final CurrentOperatorServicePort currentOperatorServicePort) {
        this.carrierRepository = carrierRepository;
        this.currentOperatorServicePort = currentOperatorServicePort;
    }

    @Override
    public List<Carrier> findCarriers(final boolean activeOnly) {
        return this.carrierRepository.findAll(this.currentOperatorServicePort.currentOperatorId(), activeOnly);
    }

    @Override
    public Carrier getCarrier(final CarrierId carrierId) {
        if (carrierId == null || carrierId.getValue() == null) {
            throw DriverHubException.invalid("Carrier id is required");
        }
        return this.carrierRepository.findById(carrierId, this.currentOperatorServicePort.currentOperatorId())
                .orElseThrow(() -> DriverHubException.notFound("Carrier " + carrierId.getValue() + " not found"));
    }

    @Override
    public Optional<Carrier> findActiveCarrier(final CarrierId carrierId) {
        if (carrierId == null || carrierId.getValue() == null) {
            return Optional.empty();
        }
        return this.carrierRepository.findActiveById(carrierId, this.currentOperatorServicePort.currentOperatorId());
    }

    @Override
    public Carrier registerCarrier(final CarrierCommand command) {
        if (command == null) {
            throw DriverHubException.invalid("Request body is required");
        }
        final Carrier carrier = this.carrierRepository.save(Carrier.register(
                this.currentOperatorServicePort.currentOperatorId(), command.name(), command.taxId(), command.phone(),
                command.email(), command.defaultTransportType()));
        log.info("Carrier {} registered", carrier.getCarrierId().getValue());
        return carrier;
    }

    @Override
    public Carrier changeCarrier(final CarrierId carrierId, final CarrierChange change) {
        final Carrier carrier = getCarrier(carrierId);
        carrier.apply(change);
        log.info("Carrier {} changed: {}", carrierId.getValue(), change.getClass().getSimpleName());
        return this.carrierRepository.save(carrier);
    }

    @Override
    public Carrier addVehicle(final CarrierId carrierId, final VehicleCommand command) {
        if (command == null) {
            throw DriverHubException.invalid("Request body is required");
        }
        final Carrier carrier = getCarrier(carrierId);
        carrier.addVehicle(command.registrationNumber(), command.brand(), command.model(), command.vehicleType(), command.trailerRegistrationNumber(),
                command.maxLoadKg());
        return this.carrierRepository.save(carrier);
    }

    @Override
    public Carrier changeVehicle(final CarrierId carrierId, final VehicleId vehicleId, final VehicleChange change) {
        final Carrier carrier = getCarrier(carrierId);
        carrier.changeVehicle(vehicleId, change);
        return this.carrierRepository.save(carrier);
    }
}
