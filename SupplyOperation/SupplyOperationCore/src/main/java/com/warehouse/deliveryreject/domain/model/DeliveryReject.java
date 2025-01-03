package com.warehouse.deliveryreject.domain.model;

import java.io.Serializable;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreject.domain.vo.Person;
import com.warehouse.deliveryreject.domain.vo.RejectReason;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity.RejectReasonEntity;
import com.warehouse.terminal.enumeration.ExecutionSourceType;
import com.warehouse.terminal.information.ExecutionSourceResolver;

public class DeliveryReject implements Serializable, ExecutionSourceResolver {
    private RejectReasonId rejectReasonId;
    private ShipmentId shipmentId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeviceId deviceId;
    private DeliveryStatus deliveryStatus;
    private RejectReason rejectReason;
    private Person recipient;

	public DeliveryReject(
            final RejectReasonId rejectReasonId,
            final ShipmentId shipmentId,
            final DepartmentCode departmentCode,
            final SupplierCode supplierCode,
            final DeviceId deviceId,
            final DeliveryStatus deliveryStatus,
            final RejectReason rejectReason,
            final Person recipient) {
        this.rejectReasonId = rejectReasonId;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deviceId = deviceId;
        this.deliveryStatus = deliveryStatus;
        this.rejectReason = rejectReason;
        this.recipient = recipient;
    }

    public static DeliveryReject from(final RejectReasonEntity rejectReason) {
        final RejectReasonId reasonId = new RejectReasonId(rejectReason.getId());
        final RejectReason reason = new RejectReason(rejectReason.getReason());
        final Person recipient = new Person(rejectReason.getRecipient(), null , null);
        return new DeliveryReject(reasonId, rejectReason.getShipmentId(), rejectReason.getRejectDepartmentCode(),
                rejectReason.getSupplierCode(), null, DeliveryStatus.valueOf(rejectReason.getDeliveryStatus()),
                reason, recipient);
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(final Person recipient) {
        this.recipient = recipient;
    }

    public RejectReasonId getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(final RejectReasonId rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
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

    public SupplierCode getSupplierCode() {
        return supplierCode;
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

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
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

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }

    public void updateRejectReasonId(final Long id) {
        this.rejectReasonId = new RejectReasonId(id);
    }
}
