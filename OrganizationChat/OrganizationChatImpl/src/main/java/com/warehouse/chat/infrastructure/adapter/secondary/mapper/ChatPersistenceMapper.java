package com.warehouse.chat.infrastructure.adapter.secondary.mapper;

import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatConversationEntity;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatMessageEntity;

public class ChatPersistenceMapper {

    public ChatConversation toDomain(final ChatConversationEntity conversation) {
        return new ChatConversation(conversation.getId(), conversation.getOperatorId(),
                new DirectChatParticipants(conversation.getFirstUserId(), conversation.getSecondUserId()),
                conversation.getCreatedAt(), conversation.getLastMessageAt());
    }

    public ChatConversationEntity toEntity(final ChatConversation conversation) {
        return new ChatConversationEntity(conversation.id(), conversation.operatorId(),
                conversation.participants().firstUserId(), conversation.participants().secondUserId(),
                conversation.createdAt(), conversation.lastMessageAt());
    }

    public ChatMessageSnapshot toDomain(final ChatMessageEntity message) {
        return new ChatMessageSnapshot(message.getId(), message.getConversationId(), message.getSenderUserId(),
                message.getClientMessageId(), message.getBody(), message.getCreatedAt());
    }

    public ChatMessageEntity toEntity(final ChatMessageSnapshot message) {
        return new ChatMessageEntity(message.id(), message.conversationId(), message.senderUserId(),
                message.clientMessageId(), message.body(), message.sentAt());
    }
}
