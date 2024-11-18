package com.warehouse.message.api;

public record MessageContentUpdateRequestDto(MessageIdDto messageId, MessageContentDto messageContent) {
}
