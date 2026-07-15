package com.warehouse.chat.application.port.secondary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationChatRepository {

    Optional<ChatConversation> findDirectConversation(final DirectChatParticipants participants);

    Optional<ChatConversation> findConversation(final ChatConversationId conversationId);

    void createConversation(final ChatConversation conversation);

    void saveConversation(final ChatConversation conversation);

    void saveMessage(final ChatMessageSnapshot message);

    Optional<ChatMessageSnapshot> findMessage(final UserId senderUserId, final UUID clientMessageId);

    List<ChatMessageSnapshot> findMessages(final ChatConversationId conversationId,
                                         final ChatMessageId beforeMessageId, final int limit);
}
