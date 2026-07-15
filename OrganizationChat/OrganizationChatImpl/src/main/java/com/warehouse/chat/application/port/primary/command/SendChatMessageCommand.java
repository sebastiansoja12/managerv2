package com.warehouse.chat.application.port.primary.command;

import java.util.UUID;

public record SendChatMessageCommand(UUID clientMessageId, String body) {
}
