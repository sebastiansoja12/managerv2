package com.warehouse.account.application.port.primary;

import com.warehouse.account.application.exception.UserInvitationNotFoundException;
import com.warehouse.account.application.port.primary.command.AcceptUserInvitationCommand;
import com.warehouse.account.application.port.primary.command.ProvisionSupplierAccountCommand;
import com.warehouse.account.application.port.secondary.NewSupplierAccount;
import com.warehouse.account.application.port.secondary.UserAccountServicePort;
import com.warehouse.account.application.port.secondary.UserInvitationRepository;
import com.warehouse.account.domain.model.UserInvitation;
import com.warehouse.account.domain.vo.InvitationToken;
import com.warehouse.account.domain.vo.UserInvitationId;
import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SupplierAccountPortImplTest {

    private static final Instant NOW = Instant.parse("2026-10-22T08:00:00Z");
    private static final SupplierId SUPPLIER_ID = new SupplierId(11L);
    private static final UserId USER_ID = new UserId(101L);

    private final UserAccountServicePort userAccountServicePort = mock(UserAccountServicePort.class);
    private final UserInvitationRepository userInvitationRepository = mock(UserInvitationRepository.class);
    private final IntegrationEventPublisher integrationEventPublisher = mock(IntegrationEventPublisher.class);

    private final SupplierAccountPortImpl port = new SupplierAccountPortImpl(
            userAccountServicePort,
            userInvitationRepository,
            integrationEventPublisher,
            Clock.fixed(NOW, ZoneOffset.UTC),
            Duration.ofHours(72),
            "https://manager.test/set-password",
            "pl");

    @Test
    void shouldCreateSupplierUserAndSendInvitation() {
        when(userInvitationRepository.findBySupplierId(SUPPLIER_ID)).thenReturn(Optional.empty());
        when(userAccountServicePort.usernameExists("KUR-01")).thenReturn(false);
        when(userAccountServicePort.createSupplierUser(any())).thenReturn(USER_ID);

        final UserId userId = port.provisionSupplierAccount(command("adam@example.com"));

        assertEquals(USER_ID, userId);
        final ArgumentCaptor<NewSupplierAccount> account = ArgumentCaptor.forClass(NewSupplierAccount.class);
        verify(userAccountServicePort).createSupplierUser(account.capture());
        assertEquals("KUR-01", account.getValue().username());
        assertEquals("adam@example.com", account.getValue().email());
        final ArgumentCaptor<IntegrationEvent> events = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(integrationEventPublisher).publish(events.capture());
        final UserInvitationIssuedIntegrationEvent invitation = assertInstanceOf(
                UserInvitationIssuedIntegrationEvent.class, events.getValue());
        assertEquals("adam@example.com", invitation.email());
        assertTrue(invitation.invitationUrl().startsWith("https://manager.test/set-password?token="));
        assertEquals(NOW.plus(Duration.ofHours(72)), invitation.expiresAt());
        verify(userInvitationRepository).save(any(UserInvitation.class));
    }

    @Test
    void shouldUseSupplierIdInUsernameWhenSupplierCodeIsTaken() {
        when(userInvitationRepository.findBySupplierId(SUPPLIER_ID)).thenReturn(Optional.empty());
        when(userAccountServicePort.usernameExists("KUR-01")).thenReturn(true);
        when(userAccountServicePort.createSupplierUser(any())).thenReturn(USER_ID);

        port.provisionSupplierAccount(command("adam@example.com"));

        final ArgumentCaptor<NewSupplierAccount> account = ArgumentCaptor.forClass(NewSupplierAccount.class);
        verify(userAccountServicePort).createSupplierUser(account.capture());
        assertEquals("KUR-01-11", account.getValue().username());
    }

    @Test
    void shouldReturnExistingUserForAlreadyProvisionedSupplier() {
        when(userInvitationRepository.findBySupplierId(SUPPLIER_ID)).thenReturn(Optional.of(invitation("hash")));

        final UserId userId = port.provisionSupplierAccount(command("adam@example.com"));

        assertEquals(USER_ID, userId);
        verify(userAccountServicePort, never()).createSupplierUser(any());
        verify(integrationEventPublisher, never()).publish(any());
    }

    @Test
    void shouldSetPasswordWhenInvitationIsAccepted() {
        final InvitationToken token = InvitationToken.generate();
        final UserInvitation invitation = invitation(token.hash());
        when(userInvitationRepository.findByTokenHash(token.hash())).thenReturn(Optional.of(invitation));

        port.acceptInvitation(new AcceptUserInvitationCommand(token, "Secret123"));

        verify(userAccountServicePort).changePassword(OperatorId.of(7L), USER_ID, "Secret123");
        verify(userInvitationRepository).save(invitation);
        assertEquals(NOW, invitation.getAcceptedAt());
    }

    @Test
    void shouldRejectUnknownInvitationToken() {
        when(userInvitationRepository.findByTokenHash(any())).thenReturn(Optional.empty());

        assertThrows(UserInvitationNotFoundException.class,
                () -> port.acceptInvitation(new AcceptUserInvitationCommand(InvitationToken.generate(), "Secret123")));
        verify(userAccountServicePort, never()).changePassword(any(), any(), any());
    }

    private ProvisionSupplierAccountCommand command(final String email) {
        return new ProvisionSupplierAccountCommand(SUPPLIER_ID, new SupplierCode("KUR-01"), "Adam", "Nowak", email,
                new DepartmentId(3L));
    }

    private UserInvitation invitation(final String tokenHash) {
        return new UserInvitation(UserInvitationId.generate(), USER_ID, SUPPLIER_ID, "KUR-01", tokenHash,
                NOW.minusSeconds(60), NOW.plus(Duration.ofHours(1)), null, OperatorId.of(7L));
    }
}
