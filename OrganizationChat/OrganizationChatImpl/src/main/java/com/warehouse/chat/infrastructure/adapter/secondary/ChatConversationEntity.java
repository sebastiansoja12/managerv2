package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.commonassets.identificator.OperatorId;
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
        name = "chat_conversations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_chat_direct_conversation",
                columnNames = {"operator_id", "first_user_id", "second_user_id"}
        )
)
public class ChatConversationEntity extends BelongsToOperator {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private ChatConversationId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "first_user_id", nullable = false))
    private UserId firstUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "second_user_id", nullable = false))
    private UserId secondUserId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "last_message_at")
    private Instant lastMessageAt;

    protected ChatConversationEntity() {
    }

    public ChatConversationEntity(final ChatConversationId id,
                                  final OperatorId operatorId,
                                  final UserId firstUserId,
                                  final UserId secondUserId,
                                  final Instant createdAt, final Instant lastMessageAt) {
        this.id = id;
        assignOperator(operatorId);
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
    }

    public ChatConversationId getId() {
        return id;
    }

    public OperatorId getOperatorId() {
        return operatorId();
    }

    public UserId getFirstUserId() {
        return firstUserId;
    }

    public UserId getSecondUserId() {
        return secondUserId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastMessageAt() {
        return lastMessageAt;
    }
}
