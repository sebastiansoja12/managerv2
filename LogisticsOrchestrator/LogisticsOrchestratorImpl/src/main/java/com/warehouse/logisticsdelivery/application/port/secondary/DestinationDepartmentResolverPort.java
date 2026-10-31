package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;

import java.util.Optional;

public interface DestinationDepartmentResolverPort {

    Optional<DepartmentId> resolve(final RecipientAddress recipientAddress);
}
