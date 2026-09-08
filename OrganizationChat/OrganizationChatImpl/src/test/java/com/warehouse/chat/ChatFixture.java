package com.warehouse.chat;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public final class ChatFixture {

    public static final Instant NOW = Instant.parse("2026-09-07T18:30:00Z");
    public static final UserId CURRENT_USER_ID = new UserId(1L);
    public static final UserId PARTICIPANT_USER_ID = new UserId(2L);
    public static final OperatorId OPERATOR_ID = OperatorId.of(10L);
    public static final ChatConversationId CONVERSATION_ID = ChatConversationId.of(5L);
    public static final UUID CLIENT_MESSAGE_ID = UUID.fromString("db7147e5-dc97-4a18-86d8-c1bde8ce988c");
    public static final DirectChatParticipants PARTICIPANTS = new DirectChatParticipants(CURRENT_USER_ID, PARTICIPANT_USER_ID);

    private ChatFixture() {
    }

    public static ChatConversation conversation() {
        return new ChatConversation(CONVERSATION_ID, OPERATOR_ID, PARTICIPANTS, NOW, null);
    }

    public static ChatMessageSnapshot message(final long id) {
        return new ChatMessageSnapshot(ChatMessageId.of(id), CONVERSATION_ID, CURRENT_USER_ID,
                CLIENT_MESSAGE_ID, "Ready", NOW);
    }
}
