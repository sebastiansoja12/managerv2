package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.delivery.domain.vo.ReturnToken;

import lombok.Builder;


@Builder
public class DeliveryReturnResponseDetails {
    private ProcessId processId;
    private DeliveryId deliveryId;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private ShipmentId shipmentId;
    private DeliveryStatus deliveryStatus;
    private ReturnToken returnToken;
    private UpdateStatus updateStatus;

    public DeliveryReturnResponseDetails(final ProcessId processId,
                                         final DeliveryId deliveryId,
                                         final DepartmentCode departmentCode,
                                         final SupplierCode supplierCode,
                                         final ShipmentId shipmentId,
                                         final DeliveryStatus deliveryStatus,
                                         final ReturnToken returnToken,
                                         final UpdateStatus updateStatus) {
        this.processId = processId;
        this.deliveryId = deliveryId;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
    }

    public static DeliveryReturnResponseDetails from(final DeliveryReturn deliveryReturn, final UpdateStatus updateStatus) {
        final ProcessId id = deliveryReturn.getProcessId() == null ? null : new ProcessId(deliveryReturn.getProcessId().toString());
        final ShipmentId shipmentIdentificator = new ShipmentId(deliveryReturn.getShipmentId());
        final DeliveryStatus status = DeliveryStatus.valueOf(deliveryReturn.getDeliveryStatus());
        final ReturnToken token = new ReturnToken(deliveryReturn.getToken());
        final DepartmentCode departmentCode = new DepartmentCode(deliveryReturn.getDepartmentCode());
        final SupplierCode supplierCode = new SupplierCode(deliveryReturn.getSupplierCode());
        return new DeliveryReturnResponseDetails(id, null, departmentCode, supplierCode,
                shipmentIdentificator, status, token, updateStatus);
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public UpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }
}
