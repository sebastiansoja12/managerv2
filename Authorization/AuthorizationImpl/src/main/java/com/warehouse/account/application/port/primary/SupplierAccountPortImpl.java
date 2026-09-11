package com.warehouse.account.application.port.primary;

import com.warehouse.account.application.exception.UserInvitationNotFoundException;
import com.warehouse.account.application.port.primary.command.AcceptUserInvitationCommand;
import com.warehouse.account.application.port.primary.command.ProvisionSupplierAccountCommand;
import com.warehouse.account.application.port.primary.result.UserInvitationResult;
import com.warehouse.account.application.port.secondary.NewSupplierAccount;
import com.warehouse.account.application.port.secondary.UserAccountServicePort;
import com.warehouse.account.application.port.secondary.UserInvitationRepository;
import com.warehouse.account.domain.model.UserInvitation;
import com.warehouse.account.domain.vo.InvitationToken;
import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
public class SupplierAccountPortImpl implements SupplierAccountPort {

    private final UserAccountServicePort userAccountServicePort;
    private final UserInvitationRepository userInvitationRepository;
    private final IntegrationEventPublisher integrationEventPublisher;
    private final Clock clock;
    private final Duration invitationValidity;
    private final String invitationUrl;
    private final String language;

    public SupplierAccountPortImpl(final UserAccountServicePort userAccountServicePort,
                                   final UserInvitationRepository userInvitationRepository,
                                   final IntegrationEventPublisher integrationEventPublisher,
                                   final Clock clock,
                                   final Duration invitationValidity,
                                   final String invitationUrl,
                                   final String language) {
        this.userAccountServicePort = userAccountServicePort;
        this.userInvitationRepository = userInvitationRepository;
        this.integrationEventPublisher = integrationEventPublisher;
        this.clock = clock;
        this.invitationValidity = invitationValidity;
        this.invitationUrl = invitationUrl;
        this.language = language;
    }

    @Override
    @Transactional
    public UserId provisionSupplierAccount(final ProvisionSupplierAccountCommand command) {
        final Optional<UserInvitation> existingInvitation = this.userInvitationRepository
                .findBySupplierId(command.supplierId());
        if (existingInvitation.isPresent()) {
            log.info("User account for supplier {} already exists", command.supplierId().value());
            return existingInvitation.get().getUserId();
        }
        final String username = availableUsername(command);
        final UserId userId = this.userAccountServicePort.createSupplierUser(new NewSupplierAccount(
                username,
                command.firstName(),
                command.lastName(),
                command.email(),
                command.departmentId(),
                this.language));
        final Instant timestamp = this.clock.instant();
        final InvitationToken token = InvitationToken.generate();
        final UserInvitation invitation = UserInvitation.issue(userId, command.supplierId(), username, token.hash(),
                timestamp, this.invitationValidity);
        this.userInvitationRepository.save(invitation);
        this.integrationEventPublisher.publish(new UserInvitationIssuedIntegrationEvent(
                userId,
                command.email(),
                command.firstName(),
                username,
                this.invitationUrl + "?token=" + token.value(),
                this.language,
                invitation.getExpiresAt(),
                timestamp));
        log.info("User account created for supplier {}", command.supplierId().value());
        return userId;
    }

    @Override
    public UserInvitationResult getInvitation(final InvitationToken token) {
        final UserInvitation invitation = findInvitation(token);
        invitation.ensureUsable(this.clock.instant());
        return new UserInvitationResult(invitation.getUsername(), invitation.getExpiresAt());
    }

    @Override
    @Transactional
    public void acceptInvitation(final AcceptUserInvitationCommand command) {
        final UserInvitation invitation = findInvitation(command.token());
        invitation.accept(command.password(), this.clock.instant());
        this.userAccountServicePort.changePassword(invitation.getOperatorId(), invitation.getUserId(),
                command.password());
        this.userInvitationRepository.save(invitation);
    }

    private UserInvitation findInvitation(final InvitationToken token) {
        return this.userInvitationRepository.findByTokenHash(token.hash())
                .orElseThrow(UserInvitationNotFoundException::new);
    }

    private String availableUsername(final ProvisionSupplierAccountCommand command) {
        final String supplierCode = command.supplierCode().value();
        if (!this.userAccountServicePort.usernameExists(supplierCode)) {
            return supplierCode;
        }
        return supplierCode + "-" + command.supplierId().value();
    }
}
