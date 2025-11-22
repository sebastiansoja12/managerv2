package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

public class ModelToEntityMapper {

    public static SupplierEntity map(final Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return new SupplierEntity(
                supplier.getSupplierId(),
                supplier.supplierCode(),
                supplier.getFirstName(),
                supplier.getLastName(),
                supplier.getTelephoneNumber(),
                supplier.getStatus(),
                supplier.getUserStatus(),
                supplier.getVehicleId(),
                supplier.getDeviceId(),
                null,
                supplier.getDriverLicense(),
                null,
                supplier.getSupportedPackageTypes(),
                supplier.getApiKey(),
                supplier.termsAccepted(),
                supplier.createdUserId(),
                supplier.getCreatedAt(),
                supplier.getUpdatedAt()
        );
    }
}


