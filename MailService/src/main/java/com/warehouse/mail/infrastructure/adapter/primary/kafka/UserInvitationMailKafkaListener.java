package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.infrastructure.adapter.primary.mapper.UserInvitationMailRequestMapper;

public class UserInvitationMailKafkaListener {

    private final MailPort mailPort;
    private final UserInvitationMailRequestMapper userInvitationMailRequestMapper;

    public UserInvitationMailKafkaListener(final MailPort mailPort,
                                           final UserInvitationMailRequestMapper userInvitationMailRequestMapper) {
        this.mailPort = mailPort;
        this.userInvitationMailRequestMapper = userInvitationMailRequestMapper;
    }

    @IntegrationEventListener(group = "manager-mail-user-invitation-issued")
    public void handle(final UserInvitationIssuedIntegrationEvent event) {
        this.mailPort.send(this.userInvitationMailRequestMapper.map(event));
    }
}
