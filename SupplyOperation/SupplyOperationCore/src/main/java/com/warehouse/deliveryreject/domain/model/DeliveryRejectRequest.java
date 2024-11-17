package com.warehouse.deliveryreject.domain.model;

import java.io.Serializable;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.terminal.enumeration.ExecutionSourceType;
import com.warehouse.terminal.information.ExecutionSourceResolver;

public class DeliveryRejectRequest implements Serializable, ExecutionSourceResolver {
    private DeliveryId deliveryId;
    private ShipmentId shipmentId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeviceId deviceId;
    private DeliveryStatus deliveryStatus;
    private RejectReason rejectReason;

	public DeliveryRejectRequest(final DeliveryId deliveryId, final ShipmentId shipmentId,
			final DepartmentCode departmentCode, final SupplierCode supplierCode, final DeviceId deviceId,
			final DeliveryStatus deliveryStatus, final RejectReason rejectReason) {
        this.deliveryId = deliveryId;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deviceId = deviceId;
        this.deliveryStatus = deliveryStatus;
        this.rejectReason = rejectReason;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(final DeliveryId deliveryId) {
        this.deliveryId = deliveryId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }
}
