package com.warehouse.chat.infrastructure.adapter.primary.mapper;

import com.warehouse.chat.application.port.primary.result.OpenChatConversationResult;
import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.infrastructure.adapter.primary.api.ChatConversationDto;
import com.warehouse.chat.infrastructure.adapter.primary.api.ChatMessageDto;

public class ChatResponseMapper {

    public ChatConversationDto toResponse(final OpenChatConversationResult result) {
        final ChatConversation conversation = result.conversation();
        return new ChatConversationDto(conversation.id(), conversation.participants().otherUser(result.currentUserId()),
                conversation.createdAt(), conversation.lastMessageAt());
    }

    public ChatMessageDto toResponse(final ChatMessageSnapshot message) {
        return new ChatMessageDto(message.id(), message.conversationId(), message.senderUserId(),
                message.clientMessageId(), message.body(), message.sentAt());
    }

    public com.warehouse.chat.api.ChatConversationDto toApi(final OpenChatConversationResult result) {
        final ChatConversation conversation = result.conversation();
        return new com.warehouse.chat.api.ChatConversationDto(conversation.id(),
                conversation.participants().otherUser(result.currentUserId()), conversation.createdAt(), conversation.lastMessageAt());
    }

    public com.warehouse.chat.api.ChatMessageDto toApi(final ChatMessageSnapshot message) {
        return new com.warehouse.chat.api.ChatMessageDto(message.id(), message.conversationId(), message.senderUserId(),
                message.clientMessageId(), message.body(), message.sentAt());
    }
}
