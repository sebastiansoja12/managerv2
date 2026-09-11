package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
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

    @KafkaEventListener(
            topics = "${manager.kafka.topics.user-invitation-issued:user.invitation.issued}",
            groupId = "${manager.mail.kafka.consumer-groups.user-invitation-issued:manager-mail-user-invitation-issued}"
    )
    public void handle(final UserInvitationIssuedIntegrationEvent event) {
        this.mailPort.send(this.userInvitationMailRequestMapper.map(event));
    }
}
