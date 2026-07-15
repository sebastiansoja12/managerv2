package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.domain.vo.ChatPresence;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryChatPresenceStoreTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(42L);
    private static final UserId USER_ID = new UserId(1L);

    private final InMemoryChatPresenceStore store = new InMemoryChatPresenceStore();

    @Test
    void shouldKeepUserOnlineUntilTheirLastWebSocketSessionDisconnects() {
        store.connect(new ChatPresence("session-1", USER_ID, OPERATOR_ID));
        store.connect(new ChatPresence("session-2", USER_ID, OPERATOR_ID));

        store.disconnect("session-1");

        assertThat(store.findOnlineUsers(OPERATOR_ID)).containsExactly(USER_ID);
        assertThat(store.disconnect("session-2")).contains(
                new ChatPresence("session-2", USER_ID, OPERATOR_ID));
        assertThat(store.findOnlineUsers(OPERATOR_ID)).isEmpty();
    }

    @Test
    void shouldReturnOnlyUsersConnectedToTheRequestedOrganization() {
        final OperatorId otherOperatorId = OperatorId.of(99L);
        final UserId otherUserId = new UserId(2L);
        store.connect(new ChatPresence("session-1", USER_ID, OPERATOR_ID));
        store.connect(new ChatPresence("session-2", otherUserId, otherOperatorId));

        assertThat(store.findOnlineUsers(OPERATOR_ID)).containsExactly(USER_ID);
        assertThat(store.findOnlineUsers(otherOperatorId)).containsExactly(otherUserId);
    }
}
