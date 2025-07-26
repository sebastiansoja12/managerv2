package com.warehouse.shipment.infrastructure.adapter.secondary.notifier;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ShipmentCreatedRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RouteTrackerNotifier {

    private final RestClient restClient;

    public RouteTrackerNotifier(final RestClient restClient) {
        this.restClient = restClient;
    }

    public void notifyShipmentCreated(final ShipmentSnapshot snapshot) {
        final ShipmentCreatedRequest request = ShipmentCreatedRequest.from(snapshot);
    }
}
