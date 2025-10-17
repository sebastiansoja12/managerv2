package com.warehouse.department.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.DepartmentCreate;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.UpdateAddressRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.AddressApi;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApi;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateAddressApiRequest;

public abstract class RequestMapper {

    public static DepartmentCreateRequest map(final DepartmentCreateApiRequest request) {
        final List<DepartmentCreate> deps = map(request.departments());
        return new DepartmentCreateRequest(deps);
    }

	private static List<DepartmentCreate> map(final List<DepartmentCreateApi> deps) {
		return deps.stream()
				.map(dep -> new DepartmentCreate(new DepartmentCode(dep.departmentCode().value()), dep.city(),
						dep.street(), dep.country(), dep.postalCode(), dep.nip(), dep.telephoneNumber(),
						dep.openingHours(), CountryCode.valueOf(dep.countryCode()),
						DepartmentType.valueOf(dep.departmentType())))
				.toList();
	}

	public static UpdateAddressRequest map(final UpdateAddressApiRequest request) {
		return new UpdateAddressRequest(new DepartmentCode(request.departmentCode().value()), map(request.address()));
	}

	private static Address map(final AddressApi address) {
		return new Address(address.city(), address.street(), address.country(), address.postalCode(), CountryCode.valueOf(address.countryCode()));
	}
}
