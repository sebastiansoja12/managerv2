package com.warehouse.message.api;

public record SenderUpdateRequestDto(MessageIdDto messageId, SenderDto sender) {
}
