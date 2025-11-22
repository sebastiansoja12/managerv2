package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

public class EntityToModelMapper {
    public static Supplier map(final SupplierEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Supplier(
                entity.getSupplierId(),
                entity.getSupplierCode(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getTelephoneNumber(),
                entity.departmentCode(),
                entity.getStatus(),
                entity.getUserStatus(),
                entity.getVehicleId(),
                entity.getDeviceId(),
                null,
                entity.getDriverLicense(),
                null,
                entity.getSupportedPackageTypes(),
                entity.getApiKey(),
                entity.getTermsAccepted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
