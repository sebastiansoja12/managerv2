package com.warehouse.chat.domain.model;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

import static com.warehouse.chat.domain.exception.ChatException.Reason.CONFLICT;
import static com.warehouse.chat.domain.exception.ChatException.Reason.INVALID_REQUEST;

public class ChatConversation {

    private final ChatConversationId id;
    private final OperatorId operatorId;
    private final DirectChatParticipants participants;
    private final Instant createdAt;
    private Instant lastMessageAt;

    public ChatConversation(final ChatConversationId id, final OperatorId operatorId,
                            final DirectChatParticipants participants, final Instant createdAt,
                            final Instant lastMessageAt) {
        this.id = id;
        this.operatorId = operatorId;
        this.participants = participants;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
    }

    public static ChatConversation open(final OperatorId operatorId, final DirectChatParticipants participants,
                                        final Instant createdAt) {
        return new ChatConversation(ChatConversationId.generate(), operatorId, participants, createdAt, null);
    }

    public ChatMessageSnapshot sendMessage(final UserId senderUserId, final UUID clientMessageId,
                                           final String body, final Instant sentAt) {
        participants.requireParticipant(senderUserId);
        if (clientMessageId == null || body == null || body.isBlank() || body.length() > 2000) {
            throw new ChatException(INVALID_REQUEST, "A chat message requires a client identifier and 1 to 2000 characters");
        }
        lastMessageAt = sentAt;
        return new ChatMessageSnapshot(ChatMessageId.generate(), id, senderUserId, clientMessageId, body.trim(), sentAt);
    }

    public ChatMessageSnapshot reuseMessage(final ChatMessageSnapshot message) {
        if (!id.equals(message.conversationId())) {
            throw new ChatException(CONFLICT, "Client message identifier is already used in another conversation");
        }
        return message;
    }

    public void requireParticipant(final UserId userId) {
        participants.requireParticipant(userId);
    }

    public ChatConversationId id() {
        return id;
    }

    public OperatorId operatorId() {
        return operatorId;
    }

    public DirectChatParticipants participants() {
        return participants;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant lastMessageAt() {
        return lastMessageAt;
    }

}
