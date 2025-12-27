package com.warehouse.returning.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.returning.domain.event.ReturnPackageChanged;
import com.warehouse.returning.domain.port.secondary.ShipmentNotifyClientPort;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReturnPackageEventListener {

    private final ReturnService returnService;

    private final ShipmentNotifyClientPort shipmentNotifyClientPort;

    private final String messageLog = "Shipment %s updated successfully";

    public ReturnPackageEventListener(final ReturnService returnService,
                                      final ShipmentNotifyClientPort shipmentNotifyClientPort) {
        this.returnService = returnService;
        this.shipmentNotifyClientPort = shipmentNotifyClientPort;
    }

    @EventListener
    public void handle(final ReturnPackageChanged event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
//        final Result<ResponseStatus, ErrorCode> result = this.shipmentNotifyClientPort.notifyReturnChanged(snapshot);
//        if (result.isSuccess()) {
//            log.info(String.format("Shipment %s updated successfully", snapshot.shipmentId().value()));
//        } else if (result.isFailure()) {
//            log.error(String.format("Shipment %s failed to update", snapshot.shipmentId().value()));
//        }
    }
}
