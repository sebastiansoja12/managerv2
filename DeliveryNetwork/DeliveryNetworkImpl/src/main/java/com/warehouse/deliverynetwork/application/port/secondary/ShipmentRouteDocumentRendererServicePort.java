package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentData;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;

public interface ShipmentRouteDocumentRendererServicePort {

    ShipmentRouteDocumentFile renderRouteDocument(final ShipmentRouteDocumentData data);

    ShipmentRouteDocumentFile renderHandlingPlan(final ShipmentRouteDocumentData data);
}
