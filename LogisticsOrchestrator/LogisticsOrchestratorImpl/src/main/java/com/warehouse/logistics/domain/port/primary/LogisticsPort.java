package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.model.CreateDeliveryCommand;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;

import java.util.List;
import java.util.Set;

public interface LogisticsPort {

    Set<LogisticsResponse> processDelivery(final Set<LogisticsRequest> logisticsRequest);

    void createDelivery(final CreateDeliveryCommand command);

    List<Delivery> findRecentDeliveries(final int offset, final int limit);

    Delivery findDelivery(final DeliveryId deliveryId);

    Delivery findDeliveryByShipmentId(final ShipmentId shipmentId);

    Delivery changeDeliveryMethod(final DeliveryId deliveryId, final DeliveryMethod method);

    void cancelDelivery(final DeliveryId deliveryId);
}
