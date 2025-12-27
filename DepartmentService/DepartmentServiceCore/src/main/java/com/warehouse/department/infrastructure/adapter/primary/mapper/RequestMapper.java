package com.warehouse.department.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.DepartmentCreate;
import com.warehouse.department.domain.model.DepartmentCreateCommand;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.IdentificationNumberChangeCommand;
import com.warehouse.department.domain.vo.UpdateAddressCommand;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.*;

import java.util.List;

public abstract class RequestMapper {

    public static DepartmentCreateCommand map(final DepartmentCreateApiRequest request) {
        final List<DepartmentCreate> deps = map(request.departments());
        return new DepartmentCreateCommand(deps);
    }

	private static List<DepartmentCreate> map(final List<DepartmentCreateApi> deps) {
		return deps.stream()
				.map(dep -> new DepartmentCreate(new DepartmentCode(dep.departmentCode().value()), dep.city(),
						dep.street(), dep.postalCode(), dep.nip(), dep.telephoneNumber(),
						dep.openingHours(), dep.email(), CountryCode.valueOf(dep.countryCode()),
						DepartmentType.valueOf(dep.departmentType())))
				.toList();
	}

	public static UpdateAddressCommand map(final UpdateAddressApiRequest request) {
		return new UpdateAddressCommand(new DepartmentCode(request.departmentCode().value()), map(request.address()));
	}

	private static Address map(final AddressApi address) {
		return new Address(address.city(), address.street(), address.postalCode(), CountryCode.valueOf(address.countryCode()));
	}

    public static IdentificationNumberChangeCommand map(final IdentificationNumberChangeApiRequest identificationNumberChangeRequest) {
		return new IdentificationNumberChangeCommand(
				new DepartmentCode(identificationNumberChangeRequest.departmentCode().value()),
				identificationNumberChangeRequest.identificationNumber());
    }
}
