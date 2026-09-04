package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.mail.application.port.secondary.MailSenderServicePort;
import com.warehouse.mail.domain.model.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisabledMailServiceAdapter implements MailSenderServicePort {

    private static final Logger log = LoggerFactory.getLogger(DisabledMailServiceAdapter.class);

    @Override
    public void send(final MailMessage mailMessage) {
        log.warn("Mail delivery is disabled (manager.mail.enabled=false), e-mail '{}' was not sent",
                mailMessage.subject());
    }
}
