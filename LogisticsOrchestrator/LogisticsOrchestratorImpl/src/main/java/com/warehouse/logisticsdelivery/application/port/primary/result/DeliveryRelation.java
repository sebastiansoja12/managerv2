package com.warehouse.logisticsdelivery.application.port.primary.result;

import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryRelationType;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

public record DeliveryRelation(DeliveryRelationType relation, DeliverySnapshot delivery) {
}
