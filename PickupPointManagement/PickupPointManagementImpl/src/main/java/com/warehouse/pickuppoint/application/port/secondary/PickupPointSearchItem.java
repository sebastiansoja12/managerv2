package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;

import java.util.Set;

public record PickupPointSearchItem(
        PickupPointId pickupPointId,
        String code,
        String name,
        PickupPointType type,
        PickupPointStatus status,
        Set<PickupPointCapability> capabilities,
        PickupPointAddress address,
        GeoCoordinates coordinates,
        DepartmentId departmentId,
        Set<PickupPointShipmentSize> allowedShipmentSizes,
        boolean acceptsDangerousGoods,
        String externalNetworkCode,
        long version) {
}
