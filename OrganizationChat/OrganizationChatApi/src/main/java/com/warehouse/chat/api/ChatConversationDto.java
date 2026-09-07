package com.warehouse.chat.api;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public record ChatConversationDto(ChatConversationId id,
                                  UserId participantUserId,
                                  Instant createdAt,
                                  Instant lastMessageAt) {
}
