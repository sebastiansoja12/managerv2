package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.palletmanagement.domain.vo.TransportCarrier;

import java.util.Optional;

public interface CarrierServicePort {
    Optional<TransportCarrier> findActiveCarrier(final CarrierId carrierId);
}
