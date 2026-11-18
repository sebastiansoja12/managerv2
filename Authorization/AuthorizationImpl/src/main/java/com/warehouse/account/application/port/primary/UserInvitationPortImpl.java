package com.warehouse.account.application.port.primary;

import com.warehouse.account.application.port.primary.command.InviteUserCommand;
import com.warehouse.account.application.port.secondary.UserInvitationRepository;
import com.warehouse.account.domain.model.UserInvitation;
import com.warehouse.account.domain.vo.InvitationToken;
import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Slf4j
public class UserInvitationPortImpl implements UserInvitationPort {

    private final UserInvitationRepository userInvitationRepository;
    private final IntegrationEventPublisher integrationEventPublisher;
    private final Clock clock;
    private final Duration invitationValidity;
    private final String invitationUrl;

    public UserInvitationPortImpl(final UserInvitationRepository userInvitationRepository,
                                  final IntegrationEventPublisher integrationEventPublisher,
                                  final Clock clock,
                                  final Duration invitationValidity,
                                  final String invitationUrl) {
        this.userInvitationRepository = userInvitationRepository;
        this.integrationEventPublisher = integrationEventPublisher;
        this.clock = clock;
        this.invitationValidity = invitationValidity;
        this.invitationUrl = invitationUrl;
    }

    @Override
    @Transactional
    public void invite(final InviteUserCommand command) {
        final Instant timestamp = this.clock.instant();
        final InvitationToken token = InvitationToken.generate();
        final UserInvitation invitation = UserInvitation.issue(command.userId(), command.username(), token.hash(),
                timestamp, this.invitationValidity);
        this.userInvitationRepository.save(invitation);
        this.integrationEventPublisher.publish(new UserInvitationIssuedIntegrationEvent(
                command.userId(),
                command.email(),
                command.firstName(),
                command.username(),
                this.invitationUrl + "?token=" + token.value(),
                command.language(),
                invitation.getExpiresAt(),
                timestamp));
        log.info("User invitation issued for user {}", command.userId().value());
    }
}
