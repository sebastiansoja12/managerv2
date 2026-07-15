package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
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
import java.util.UUID;

@Entity
@Table(
        name = "chat_messages",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_chat_message_client_id",
                columnNames = {"operator_id", "sender_user_id", "client_message_id"}
        )
)
public class ChatMessageEntity extends BelongsToOperator {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false))
    private ChatMessageId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "conversation_id", nullable = false))
    private ChatConversationId conversationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sender_user_id", nullable = false))
    private UserId senderUserId;

    @Column(name = "client_message_id", nullable = false)
    private UUID clientMessageId;

    @Column(name = "body", nullable = false, length = 2000)
    private String body;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ChatMessageEntity() {
    }

    public ChatMessageEntity(final ChatMessageId id,
                                  final ChatConversationId conversationId,
                             final UserId senderUserId,
                             final UUID clientMessageId,
                             final String body,
                             final Instant createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderUserId = senderUserId;
        this.clientMessageId = clientMessageId;
        this.body = body;
        this.createdAt = createdAt;
    }

    public ChatMessageId getId() {
        return id;
    }

    public ChatConversationId getConversationId() {
        return conversationId;
    }

    public UserId getSenderUserId() {
        return senderUserId;
    }

    public UUID getClientMessageId() {
        return clientMessageId;
    }

    public String getBody() {
        return body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
