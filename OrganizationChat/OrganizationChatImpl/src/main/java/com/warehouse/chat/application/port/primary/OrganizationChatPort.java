package com.warehouse.chat.application.port.primary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.application.port.primary.command.SendChatMessageCommand;
import com.warehouse.chat.application.port.primary.result.OpenChatConversationResult;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface OrganizationChatPort {

    void connectPresence(final String sessionId, final UserId userId);

    void disconnectPresence(final String sessionId);

    OpenChatConversationResult openDirectConversation(final UserId participantUserId);

    List<ChatMessageSnapshot> findMessages(final ChatConversationId conversationId,
                                         final ChatMessageId beforeMessageId, final int limit);

    ChatMessageSnapshot sendMessage(final ChatConversationId conversationId, final SendChatMessageCommand command);
}
