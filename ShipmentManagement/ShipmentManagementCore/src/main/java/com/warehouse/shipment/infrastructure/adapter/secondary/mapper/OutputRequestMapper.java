package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.reroute.infrastructure.adapter.secondary.api.ShipmentIdDto;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class OutputRequestMapper {
	public static ReturnRequestApi map(final ShipmentSnapshot snapshot) {
		final List<ReturnPackageRequestApi> requests = new ArrayList<>();
		requests.add(new ReturnPackageRequestApi(new ShipmentIdDto(snapshot.shipmentId().getValue()), "SYSTEM",
				getCurrentDepartmentCode(), new UserIdApi(getCurrentUser().value()), new ReasonCodeApi("NO_LONGER_NEEDED")
		));
		return new ReturnRequestApi(requests);
	}

    public static DepartmentCodeApi getCurrentDepartmentCode() {
        return new DepartmentCodeApi(getDepartmentCode().value());
    }

    private static DepartmentCode getDepartmentCode() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
            return token.getDepartmentCode();
        } else {
            throw new IllegalStateException("No department code found");
        }
    }

    private static UserId getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
            return (UserId) token.getPrincipal();
        } else {
            throw new IllegalStateException("No user found");
        }
    }
}
