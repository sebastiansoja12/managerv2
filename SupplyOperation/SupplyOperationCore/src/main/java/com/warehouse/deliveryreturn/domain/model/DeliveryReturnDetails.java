package com.warehouse.deliveryreturn.domain.model;

import static com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus.RETURN;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.exception.WrongDeliveryStatusException;

import lombok.Builder;

@Builder
public class DeliveryReturnDetails {
    private ShipmentId shipmentId;
    private DeliveryStatus deliveryStatus;
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private String token;

    public DeliveryReturnDetails(final ShipmentId shipmentId,
                                 final DeliveryStatus deliveryStatus,
                                 final DepartmentCode departmentCode,
                                 final SupplierCode supplierCode,
                                 final String token) {
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.departmentCode = departmentCode;
        this.supplierCode = supplierCode;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void validateDeliveryStatus() {
        if (!RETURN.equals(deliveryStatus)) {
            throw new WrongDeliveryStatusException(500, "Wrong delivery status");
        }
    }

    public DeliveryReturnDetails updateDeliveryStatus() {
        return DeliveryReturnDetails.builder()
                .deliveryStatus(RETURN)
                .shipmentId(shipmentId)
                .supplierCode(supplierCode)
                .departmentCode(departmentCode)
                .build();
    }
}
