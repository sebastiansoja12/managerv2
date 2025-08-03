package com.warehouse.shipment.infrastructure.adapter.secondary.notifier;

import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.vo.ShipmentHistoryTracker;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RouteTrackerHistoryNotifier {

    public void notifyShipmentRoute(final ShipmentHistoryTracker historyTracker) {
        if (historyTracker.successful()) {
            log.info("Shipment {} has been successfully registered in route tracker at {}", historyTracker.shipmentId().getValue(),
                    historyTracker.timestamp());
        } else {
			log.error("Shipment {} has not been registered in route tracker at {}, error cause {}, {}",
					historyTracker.shipmentId().getValue(), historyTracker.timestamp(), historyTracker.errorCause(),
					historyTracker.errorMessage());
        }
    }
}
