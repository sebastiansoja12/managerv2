package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.vo.DeliverableShipment;

public interface MailServicePort {
    void sendNotification(final DeliverableShipment deliverableShipment);
}
