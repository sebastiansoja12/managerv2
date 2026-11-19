package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.domain.vo.DeliveryArea;

public record ChangeSupplierDeliveryAreaCommand(SupplierCode supplierCode, DeliveryArea deliveryArea) {
}
