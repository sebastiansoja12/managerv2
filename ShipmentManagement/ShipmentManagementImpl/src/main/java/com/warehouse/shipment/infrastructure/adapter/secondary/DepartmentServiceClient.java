package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;

public class DepartmentServiceClient implements DepartmentServicePort {

	private final DepartmentApiService departmentApiService;

	public DepartmentServiceClient(final DepartmentApiService departmentApiService) {
		this.departmentApiService = departmentApiService;
	}

	@Override
	public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
		final DepartmentDto department = this.departmentApiService.getDepartmentById(departmentId);
		return new DepartmentCode(department.departmentCode());
	}

	@Override
	public DepartmentId getDepartmentId(final DepartmentCode departmentCode) {
		return new DepartmentId(this.departmentApiService.getDepartmentByCode(departmentCode).departmentId());
	}
}
