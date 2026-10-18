package com.warehouse.mail.application.port.secondary;

import com.warehouse.mail.domain.model.MailMessage;

public interface MailSenderServicePort {
    void send(final MailMessage mailMessage);
}
