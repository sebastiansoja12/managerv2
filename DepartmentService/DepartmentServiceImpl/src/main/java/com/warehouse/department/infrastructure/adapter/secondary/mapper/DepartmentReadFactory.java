package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.Coordinates;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentAddress;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentCoordinates;
import com.warehouse.department.infrastructure.adapter.secondary.entity.TaxId;
import com.warehouse.department.infrastructure.adapter.secondary.entity.readmodel.DepartmentReadEntity;
import org.springframework.stereotype.Component;

@Component
public class DepartmentReadFactory {

    public DepartmentReadEntity fromDepartmentSnapshot(final DepartmentSnapshot snapshot) {
        final DepartmentReadEntity readEntity = new DepartmentReadEntity(
                snapshot.departmentCode(),
                map(snapshot.address()),
                new TaxId(snapshot.taxId().value()),
                snapshot.telephoneNumber(),
                snapshot.openingHours(),
                snapshot.email(),
                DepartmentReadEntity.DepartmentType.valueOf(snapshot.departmentType().name()),
                DepartmentReadEntity.Status.valueOf(snapshot.status().name()),
                map(snapshot.coordinates()),
                snapshot.createdAt(),
                snapshot.updatedAt(),
                snapshot.adminUserId(),
                snapshot.createdBy(),
                snapshot.lastModifiedBy()
        );
        readEntity.assignOperator(snapshot.operatorId());
        return readEntity;
    }

    private DepartmentAddress map(final Address address) {
        return new DepartmentAddress(address.city(), address.postalCode(), address.street(), address.countryCode());
    }

    private DepartmentCoordinates map(final Coordinates coordinates) {
        return new DepartmentCoordinates(coordinates.lat(), coordinates.lon());
    }
}
