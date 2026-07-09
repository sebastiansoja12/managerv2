package com.warehouse.logistics.domain.model;


import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.logistics.domain.vo.RejectReason;
import com.warehouse.logistics.domain.vo.ReturnToken;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;

import lombok.Builder;


@Builder
public class LogisticsRequest {
    private ShipmentId shipmentId;
    private ShipmentId newShipmentId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private DeliveryStatus deliveryStatus;
    private ProcessType processType;
    private ReturnToken returnToken;
    private DeliveryToken deliveryToken;
    private RejectReason rejectReason;

    public LogisticsRequest(final ShipmentId shipmentId,
                            final ShipmentId newShipmentId,
                            final DepartmentCode departmentCode,
                            final SupplierCode supplierCode,
                            final DeliveryStatus deliveryStatus,
                            final ProcessType processType,
                            final ReturnToken returnToken,
                            final DeliveryToken deliveryToken,
                            final RejectReason rejectReason) {
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.processType = processType;
        this.returnToken = returnToken;
        this.deliveryToken = deliveryToken;
        this.rejectReason = rejectReason;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
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
