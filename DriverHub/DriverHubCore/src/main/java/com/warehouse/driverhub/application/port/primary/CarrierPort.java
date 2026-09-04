package com.warehouse.driverhub.application.port.primary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.application.port.primary.command.CarrierCommand;
import com.warehouse.driverhub.application.port.primary.command.VehicleCommand;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.vo.CarrierChange;
import com.warehouse.driverhub.domain.vo.VehicleChange;

import java.util.List;
import java.util.Optional;

public interface CarrierPort {
    List<Carrier> findCarriers(final boolean activeOnly);
    Carrier getCarrier(final CarrierId carrierId);
    Optional<Carrier> findActiveCarrier(final CarrierId carrierId);
    Carrier registerCarrier(final CarrierCommand command);
    Carrier changeCarrier(final CarrierId carrierId, final CarrierChange change);
    Carrier addVehicle(final CarrierId carrierId, final VehicleCommand command);
    Carrier changeVehicle(final CarrierId carrierId, final VehicleId vehicleId, final VehicleChange change);
}
