package com.warehouse.logistics.domain.model;


import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logistics.domain.vo.RejectReason;
import com.warehouse.logistics.domain.vo.ReturnToken;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;

import lombok.Builder;


public class LogisticsRequest {
    private ShipmentId shipmentId;
    private DeliveryTarget target;
    private ShipmentId newShipmentId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private SupplierId supplierId;
    private VehicleId vehicleId;
    private DeliveryStatus deliveryStatus;
    private ProcessType processType;
    private ReturnToken returnToken;
    private DeliveryToken deliveryToken;
    private RejectReason rejectReason;

    @Builder
    public LogisticsRequest(final ShipmentId shipmentId,
                            final DeliveryTarget target,
                            final ShipmentId newShipmentId,
                            final DepartmentCode departmentCode,
                            final SupplierCode supplierCode,
                            final SupplierId supplierId,
                            final VehicleId vehicleId,
                            final DeliveryStatus deliveryStatus,
                            final ProcessType processType,
                            final ReturnToken returnToken,
                            final DeliveryToken deliveryToken,
                            final RejectReason rejectReason) {
        this.target = target != null ? target : shipmentId == null ? null : DeliveryTarget.shipment(shipmentId);
        this.shipmentId = this.target == null ? shipmentId
                : this.target.type() == DeliveryTargetType.SHIPMENT
                        ? new ShipmentId(Long.valueOf(this.target.id())) : null;
        this.newShipmentId = newShipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.supplierId = supplierId;
        this.vehicleId = vehicleId;
        this.deliveryStatus = deliveryStatus;
        this.processType = processType;
        this.returnToken = returnToken;
        this.deliveryToken = deliveryToken;
        this.rejectReason = rejectReason;
    }

    public LogisticsRequest(final ShipmentId shipmentId,
                            final ShipmentId newShipmentId,
                            final DepartmentCode departmentCode,
                            final SupplierCode supplierCode,
                            final DeliveryStatus deliveryStatus,
                            final ProcessType processType,
                            final ReturnToken returnToken,
                            final DeliveryToken deliveryToken,
                            final RejectReason rejectReason) {
        this(shipmentId, null, newShipmentId, departmentCode, supplierCode, null, null,
                deliveryStatus, processType, returnToken, deliveryToken, rejectReason);
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DeliveryTarget getTarget() {
        return target != null ? target : shipmentId == null ? null : DeliveryTarget.shipment(shipmentId);
    }

    public void setTarget(final DeliveryTarget target) {
        this.target = target;
        this.shipmentId = target != null && target.type() == DeliveryTargetType.SHIPMENT
                ? new ShipmentId(Long.valueOf(target.id())) : null;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
        this.target = shipmentId == null ? null : DeliveryTarget.shipment(shipmentId);
    }

    public ShipmentId getNewShipmentId() {
        return newShipmentId;
    }

    public void setNewShipmentId(final ShipmentId newShipmentId) {
        this.newShipmentId = newShipmentId;
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

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final SupplierId supplierId) {
        this.supplierId = supplierId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(final VehicleId vehicleId) {
        this.vehicleId = vehicleId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public void setReturnToken(final ReturnToken returnToken) {
        this.returnToken = returnToken;
    }

    public DeliveryToken getDeliveryToken() {
        return deliveryToken;
    }

    public void setDeliveryToken(final DeliveryToken deliveryToken) {
        this.deliveryToken = deliveryToken;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(final RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void updateDeliveryStatus() {
        this.deliveryStatus = DeliveryStatus.DELIVERY;
    }

    public static LogisticsRequest from(final ProcessType processType,
                                        final DeliveryReturnResponseDetails returnResponseDetails) {
        return new LogisticsRequest(returnResponseDetails.getShipmentId(), null, returnResponseDetails.getDepartmentCode(),
                returnResponseDetails.getSupplierCode(), returnResponseDetails.getDeliveryStatus(),
                processType, new ReturnToken(returnResponseDetails.getReturnToken().value()), null, null);
    }

}
