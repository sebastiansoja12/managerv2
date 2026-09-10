package com.warehouse.mail.application.port.primary;

import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;

import java.util.List;

public interface MailPort {
    void send(final List<MailRequest> mailRequests);
    void send(final MailMessage mailMessage);
}
