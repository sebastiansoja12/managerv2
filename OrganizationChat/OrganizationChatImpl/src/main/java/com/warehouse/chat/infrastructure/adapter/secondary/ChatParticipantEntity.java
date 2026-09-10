package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.api.identificator.ChatParticipantId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "chat_participants",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_chat_participant",
                columnNames = {"operator_id", "conversation_id", "user_id"}
        )
)
public class ChatParticipantEntity extends BelongsToOperator {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private ChatParticipantId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "conversation_id", nullable = false))
    private ChatConversationId conversationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "last_read_message_id"))
    private ChatMessageId lastReadMessageId;

    @Column(name = "archived", nullable = false)
    private boolean archived;

    protected ChatParticipantEntity() {
    }

    public ChatParticipantEntity(final ChatConversationId conversationId,
                                 final UserId userId,
                                 final Instant joinedAt) {
        this.id = ChatParticipantId.generate();
        this.conversationId = conversationId;
        this.userId = userId;
        this.joinedAt = joinedAt;
        this.archived = false;
    }

    public ChatParticipantId getId() {
        return id;
    }

    public ChatConversationId getConversationId() {
        return conversationId;
    }

    public UserId getUserId() {
        return userId;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public ChatMessageId getLastReadMessageId() {
        return lastReadMessageId;
    }

    public boolean isArchived() {
        return archived;
    }
}
