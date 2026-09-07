package com.warehouse.chat.api;

import java.util.UUID;

public record SendChatMessageRequest(UUID clientMessageId, String body) {
}
