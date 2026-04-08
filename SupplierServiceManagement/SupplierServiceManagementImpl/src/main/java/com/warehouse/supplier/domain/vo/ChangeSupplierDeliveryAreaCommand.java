package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.domain.model.DeliveryArea;

public record ChangeSupplierDeliveryAreaCommand(SupplierCode supplierCode, DeliveryArea deliveryArea) {
}
