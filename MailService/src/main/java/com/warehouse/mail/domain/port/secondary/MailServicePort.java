package com.warehouse.mail.domain.port.secondary;

import com.warehouse.mail.domain.vo.Notification;

public interface MailServicePort {

    void sendNotification(Notification notification);
}
