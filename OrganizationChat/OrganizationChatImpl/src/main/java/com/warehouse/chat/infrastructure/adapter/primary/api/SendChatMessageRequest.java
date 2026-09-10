package com.warehouse.chat.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SendChatMessageRequest(
        @NotNull UUID clientMessageId,
        @NotBlank @Size(max = 2000) String body) {
}
