package com.warehouse.driverhub.application.port.secondary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.driverhub.domain.model.Carrier;

import java.util.List;
import java.util.Optional;

public interface CarrierRepository {
    Carrier save(final Carrier carrier);
    Optional<Carrier> findById(final CarrierId carrierId, final OperatorId operatorId);
    Optional<Carrier> findActiveById(final CarrierId carrierId, final OperatorId operatorId);
    Optional<Carrier> findByDriverId(final DriverId driverId, final OperatorId operatorId);
    List<Carrier> findAll(final OperatorId operatorId, final boolean activeOnly);
}
