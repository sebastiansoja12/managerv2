package com.warehouse.account.application.port.primary;

import com.warehouse.account.application.port.primary.command.InviteUserCommand;
import com.warehouse.account.application.port.secondary.UserInvitationRepository;
import com.warehouse.account.domain.model.UserInvitation;
import com.warehouse.auth.api.event.UserInvitationIssuedIntegrationEvent;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserInvitationPortImplTest {

    private static final Instant NOW = Instant.parse("2026-11-05T08:00:00Z");
    private static final UserId USER_ID = new UserId(202L);

    private final UserInvitationRepository userInvitationRepository = mock(UserInvitationRepository.class);
    private final IntegrationEventPublisher integrationEventPublisher = mock(IntegrationEventPublisher.class);

    private final UserInvitationPortImpl port = new UserInvitationPortImpl(
            userInvitationRepository,
            integrationEventPublisher,
            Clock.fixed(NOW, ZoneOffset.UTC),
            Duration.ofHours(72),
            "https://manager.test/set-password");

    @Test
    void shouldSaveInvitationAndPublishMailEvent() {
        port.invite(new InviteUserCommand(USER_ID, "jkowalski", "Jan", "jan@example.com", "en"));

        final ArgumentCaptor<UserInvitation> invitation = ArgumentCaptor.forClass(UserInvitation.class);
        verify(userInvitationRepository).save(invitation.capture());
        assertEquals(USER_ID, invitation.getValue().getUserId());
        assertNull(invitation.getValue().getSupplierId());
        final ArgumentCaptor<IntegrationEvent> events = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(integrationEventPublisher).publish(events.capture());
        final UserInvitationIssuedIntegrationEvent issued = assertInstanceOf(
                UserInvitationIssuedIntegrationEvent.class, events.getValue());
        assertEquals("jan@example.com", issued.email());
        assertEquals("en", issued.language());
        assertTrue(issued.invitationUrl().startsWith("https://manager.test/set-password?token="));
        assertEquals(NOW.plus(Duration.ofHours(72)), issued.expiresAt());
    }
}
