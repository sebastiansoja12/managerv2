package com.warehouse.logisticsdelivery;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

import java.util.UUID;

public final class DeliveryFixture {

    public static final UserId USER_ID = new UserId(1L);
    public static final DepartmentId ORIGIN_DEPARTMENT_ID = new DepartmentId(3L);
    public static final DepartmentId DESTINATION_DEPARTMENT_ID = new DepartmentId(5L);
    public static final PickupPointId PICKUP_POINT_ID =
            new PickupPointId(UUID.fromString("7d4bd2b0-5b55-4c0f-9a43-8a8f8a7e1d10"));
    public static final PickupPointId DELIVERY_PICKUP_POINT_ID =
            new PickupPointId(UUID.fromString("0c7a5d0e-4b0f-4c64-9f53-1b2f62c6a8b1"));

    private DeliveryFixture() {
    }

    public static Delivery shipmentDelivery(final ShipmentId shipmentId) {
        return new Delivery(DeliveryTarget.shipment(shipmentId), shipmentId, DeliveryType.OUTBOUND,
                ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID, PickupMethod.PICKUP_POINT, DeliveryMethod.LOCKER,
                PICKUP_POINT_ID, DELIVERY_PICKUP_POINT_ID, null, false, USER_ID);
    }

    public static Delivery courierShipmentDelivery(final ShipmentId shipmentId) {
        return new Delivery(DeliveryTarget.shipment(shipmentId), shipmentId, DeliveryType.OUTBOUND,
                null, null, null, DeliveryMethod.COURIER, null, null, null, false, USER_ID);
    }
}
