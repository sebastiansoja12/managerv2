package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.chat.OrganizationChatApiService;
import com.warehouse.chat.api.ChatConversationDto;
import com.warehouse.chat.api.ChatMessageDto;
import com.warehouse.chat.api.SendChatMessageRequest;
import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.chat.application.port.primary.command.SendChatMessageCommand;
import com.warehouse.chat.infrastructure.adapter.primary.mapper.ChatResponseMapper;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public class OrganizationChatApiServiceAdapter implements OrganizationChatApiService {

    private final OrganizationChatPort chatPort;
    private final ChatResponseMapper mapper;

    public OrganizationChatApiServiceAdapter(final OrganizationChatPort chatPort, final ChatResponseMapper mapper) {
        this.chatPort = chatPort;
        this.mapper = mapper;
    }

    @Override
    public ChatConversationDto openDirectConversation(final UserId participantUserId) {
        return mapper.toApi(chatPort.openDirectConversation(participantUserId));
    }

    @Override
    public List<ChatMessageDto> findMessages(final ChatConversationId conversationId,
                                           final ChatMessageId beforeMessageId, final int limit) {
        return chatPort.findMessages(conversationId, beforeMessageId, limit).stream().map(mapper::toApi).toList();
    }

    @Override
    public ChatMessageDto sendMessage(final ChatConversationId conversationId, final SendChatMessageRequest request) {
        return mapper.toApi(chatPort.sendMessage(conversationId, new SendChatMessageCommand(request.clientMessageId(), request.body())));
    }
}
