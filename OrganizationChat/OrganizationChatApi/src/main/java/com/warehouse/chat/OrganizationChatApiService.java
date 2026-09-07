package com.warehouse.chat;

import com.warehouse.chat.api.ChatConversationDto;
import com.warehouse.chat.api.ChatMessageDto;
import com.warehouse.chat.api.SendChatMessageRequest;
import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface OrganizationChatApiService {

    ChatConversationDto openDirectConversation(final UserId participantUserId);

    List<ChatMessageDto> findMessages(final ChatConversationId conversationId, final ChatMessageId beforeMessageId, final int limit);

    ChatMessageDto sendMessage(final ChatConversationId conversationId, final SendChatMessageRequest request);
}
