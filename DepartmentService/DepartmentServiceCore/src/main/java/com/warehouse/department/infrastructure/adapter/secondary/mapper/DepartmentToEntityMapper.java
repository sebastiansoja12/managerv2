package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.enumeration.DepartmentType;

public abstract class DepartmentToEntityMapper {

	public static DepartmentEntity map(final Department department) {
		return new DepartmentEntity(new DepartmentCode(department.getDepartmentCode().getValue()), department.getCity(),
				department.getStreet(), department.getCountry(), department.getPostalCode(), department.getNip(),
				department.getTelephoneNumber(), department.getOpeningHours(), department.getActive(),
				department.getCountryCode(), DepartmentType.valueOf(department.getDepartmentType().name()), null, null);
	}
}
