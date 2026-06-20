package com.warehouse.chat.application.port.primary.result;

import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.commonassets.identificator.UserId;

public record OpenChatConversationResult(ChatConversation conversation, UserId currentUserId) {
}
