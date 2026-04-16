package com.warehouse.deliverymissed.domain.vo;


import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;
import lombok.Builder;

@Builder
public final class DeliveryMissed {
    private final String deliveryId;
    private final ShipmentId shipmentId;
    private final DepartmentCode departmentCode;
    private final SupplierCode supplierCode;
    private final DeliveryStatus deliveryStatus;

    public DeliveryMissed(final String deliveryId,
                          final ShipmentId shipmentId,
                          final DepartmentCode departmentCode,
                          final SupplierCode supplierCode,
                          final DeliveryStatus deliveryStatus) {
        this.deliveryId = deliveryId;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
    }

    public static DeliveryMissed from(final DeliveryMissedEntity deliveryMissedEntity) {
        return new DeliveryMissed(deliveryMissedEntity.getId(), new ShipmentId(deliveryMissedEntity.getShipmentId()),
                new DepartmentCode(deliveryMissedEntity.getDepartmentCode()),
                new SupplierCode(deliveryMissedEntity.getSupplierCode()),
                deliveryMissedEntity.getDeliveryStatus());
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }
}
