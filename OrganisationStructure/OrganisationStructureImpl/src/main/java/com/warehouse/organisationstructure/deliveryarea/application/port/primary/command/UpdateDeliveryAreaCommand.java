package com.warehouse.organisationstructure.deliveryarea.application.port.primary.command;

import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;

public record UpdateDeliveryAreaCommand(DeliveryAreaId deliveryAreaId, DeliveryAreaName name,
                                         DeliveryAreaBoundary boundary) {
}
