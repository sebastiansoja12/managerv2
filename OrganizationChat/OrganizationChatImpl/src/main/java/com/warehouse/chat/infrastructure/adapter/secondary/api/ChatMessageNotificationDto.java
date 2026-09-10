package com.warehouse.chat.infrastructure.adapter.secondary.api;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageNotificationDto(UserId participantUserId, Message message) {

    public record Message(ChatMessageId id, ChatConversationId conversationId, UserId senderUserId,
                          UUID clientMessageId, String body, Instant sentAt) {
    }
}
