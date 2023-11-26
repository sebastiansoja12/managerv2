package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.vo.Parcel;

public interface MailServicePort {
    void sendNotification(Parcel parcel);
}
