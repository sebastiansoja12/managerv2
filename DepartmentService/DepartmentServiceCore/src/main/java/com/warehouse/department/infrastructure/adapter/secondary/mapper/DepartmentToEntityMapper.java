package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.Coordinates;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentAddress;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentCoordinates;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.entity.TaxId;

public abstract class DepartmentToEntityMapper {

	public static DepartmentEntity map(final Department department) {
		return new DepartmentEntity(new DepartmentCode(department.getDepartmentCode().getValue()),
				map(department.getAddress()), new TaxId(department.getTaxId().value()), department.getTelephoneNumber(),
				department.getOpeningHours(), department.getEmail(),
				DepartmentEntity.DepartmentType.valueOf(department.getDepartmentType().name()),
				DepartmentEntity.Status.valueOf(department.getStatus().name()),
				map(department.getCoordinates()), department.getCreatedAt(),
				department.getUpdatedAt(), department.getAdminUserId(), department.getCreatedBy(),
				department.getLastModifiedBy());
	}
    
    public static DepartmentAddress map(final Address address) {
		return new DepartmentAddress(address.city(), address.postalCode(), address.street(), address.countryCode());
    }

	public static DepartmentCoordinates map(final Coordinates coordinates) {
		return new DepartmentCoordinates(coordinates.lat(), coordinates.lon());
	}
}
