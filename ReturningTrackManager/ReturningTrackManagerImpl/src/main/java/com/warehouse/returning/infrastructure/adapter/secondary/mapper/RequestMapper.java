package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.infrastructure.adapter.secondary.api.DepartmentCodeApi;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ShipmentIdApi;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ShipmentReturnRequestApi;
import com.warehouse.returning.infrastructure.adapter.secondary.api.UserIdApi;

public class RequestMapper {
    
    public static ShipmentReturnRequestApi toShipmentReturnRequestApi(final ReturnPackageSnapshot snapshot) {
		return new ShipmentReturnRequestApi(new ShipmentIdApi(snapshot.shipmentId().value()), snapshot.reason(),
				new DepartmentCodeApi(snapshot.returnedDepartmentCode().value()),
				new UserIdApi(snapshot.processedBy().value()), snapshot.returnStatus().name());
	}
}
