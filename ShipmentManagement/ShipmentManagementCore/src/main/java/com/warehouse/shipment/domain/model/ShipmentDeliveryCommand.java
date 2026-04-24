package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;

public class ShipmentDeliveryCommand {
    private ShipmentId shipmentId;
    private DeliveryMethod deliveryMethod;
    private SupplierCode supplierCode;
    private DeliveryStatus deliveryStatus;
    
	public ShipmentDeliveryCommand(final ShipmentId shipmentId, final DeliveryMethod deliveryMethod,
			final SupplierCode supplierCode, final DeliveryStatus deliveryStatus) {
        this.shipmentId = shipmentId;
        this.deliveryMethod = deliveryMethod;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
    }
    
    public ShipmentId getShipmentId() {
        return shipmentId;
    }
    
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }
    
    public SupplierCode getSupplierCode() {
        return supplierCode;
    }
    
    public void setSupplierCode(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }
    
    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }
    
    public void setDeliveryMethod(final DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
