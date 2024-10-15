package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.vo.Shipment;

public interface MailServicePort {
    void sendNotification(Shipment shipment);
}
