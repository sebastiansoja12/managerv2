package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.vo.EmailNotification;

public interface MailServicePort {
    void sendMail(final EmailNotification emailNotification);
}
