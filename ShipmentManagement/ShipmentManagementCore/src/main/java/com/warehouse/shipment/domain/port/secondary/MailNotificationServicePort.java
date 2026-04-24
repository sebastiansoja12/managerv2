package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;

public interface MailNotificationServicePort {
    Result<Void, ErrorCode> notifyRecipient(final DeliveryStatus deliveryStatus, final Shipment shipment);
}
