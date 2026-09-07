package com.warehouse.chat.api;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageDto(ChatMessageId id,
                             ChatConversationId conversationId,
                             UserId senderUserId,
                             UUID clientMessageId,
                             String body,
                             Instant sentAt) {
}
