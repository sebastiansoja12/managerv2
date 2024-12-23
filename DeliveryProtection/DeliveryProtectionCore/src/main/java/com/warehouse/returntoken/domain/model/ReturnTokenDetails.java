package com.warehouse.returntoken.domain.model;


import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;


import lombok.Builder;

@Builder
public class ReturnTokenDetails {
    private ShipmentId shipmentId;
    private DeliveryStatus deliveryStatus;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private ReturnToken returnToken;

    public ReturnTokenDetails(final ShipmentId shipmentId,
                              final DeliveryStatus deliveryStatus,
                              final DepartmentCode departmentCode,
                              final SupplierCode supplierCode,
                              final ReturnToken returnToken) {
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.returnToken = returnToken;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public void setReturnToken(final ReturnToken returnToken) {
        this.returnToken = returnToken;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public ReturnTokenDetails updateDeliveryStatus() {
        return ReturnTokenDetails.builder()
                .deliveryStatus(DeliveryStatus.RETURN)
                .shipmentId(shipmentId)
                .supplierCode(supplierCode)
                .departmentCode(departmentCode)
                .build();
    }
}
