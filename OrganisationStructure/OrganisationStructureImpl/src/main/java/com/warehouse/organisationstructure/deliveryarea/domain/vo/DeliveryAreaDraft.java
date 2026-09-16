package com.warehouse.organisationstructure.deliveryarea.domain.vo;

import com.warehouse.commonassets.identificator.DeliveryAreaId;

public record DeliveryAreaDraft(DeliveryAreaId deliveryAreaId, DeliveryAreaName name, DeliveryAreaBoundary boundary) {
}
