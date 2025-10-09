package com.warehouse.returning.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.returning.domain.enumeration.ErrorCode;
import com.warehouse.returning.domain.enumeration.ResponseStatus;
import com.warehouse.returning.domain.event.ReturnPackageChanged;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.port.secondary.ShipmentNotifyClientPort;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

@Component
public class ReturnPackageEventListener {

    private final ReturnService returnService;

    private final ShipmentNotifyClientPort shipmentNotifyClientPort;

    public ReturnPackageEventListener(final ReturnService returnService,
                                      final ShipmentNotifyClientPort shipmentNotifyClientPort) {
        this.returnService = returnService;
        this.shipmentNotifyClientPort = shipmentNotifyClientPort;
    }

    @EventListener
    public void handle(final ReturnPackageChanged event) {
        final ReturnPackageSnapshot snapshot = event.getSnapshot();
        final Result<ResponseStatus, ErrorCode> result = this.shipmentNotifyClientPort.notifyReturnChanged(snapshot);

    }
}
