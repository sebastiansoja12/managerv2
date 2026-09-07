package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrganizationChatPresenceWebSocketAdapterTest {

    private static final UserId USER_ID = new UserId(1L);

    private final OrganizationChatPort chatPort = mock(OrganizationChatPort.class);
    private final OrganizationChatPresenceWebSocketAdapter adapter =
            new OrganizationChatPresenceWebSocketAdapter(chatPort);
    private final TestPrincipal principal = new TestPrincipal(USER_ID);

    @Test
    void shouldConnectPresenceFromStompApplicationEvent() {
        adapter.online("session-1", principal);

        verify(chatPort).connectPresence("session-1", USER_ID);
    }

    @Test
    void shouldDisconnectPresenceFromStompApplicationEvent() {
        adapter.offline("session-1", principal);

        verify(chatPort).disconnectPresence("session-1");
    }

    private record TestPrincipal(UserId userId)
            implements OrganizationChatPresenceWebSocketAdapter.UserIdPrincipal {

        @Override
        public String getName() {
            return String.valueOf(userId.value());
        }
    }
}
