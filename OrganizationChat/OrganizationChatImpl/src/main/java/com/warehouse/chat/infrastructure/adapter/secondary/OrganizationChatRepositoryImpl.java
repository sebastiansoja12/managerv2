package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.application.port.secondary.OrganizationChatRepository;
import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.chat.infrastructure.adapter.secondary.mapper.ChatPersistenceMapper;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrganizationChatRepositoryImpl implements OrganizationChatRepository {

    private final OperatorFilteredRepository<ChatConversationEntity> conversationRepository;
    private final OperatorFilteredRepository<ChatParticipantEntity> participantRepository;
    private final OperatorFilteredRepository<ChatMessageEntity> messageRepository;
    private final ChatPersistenceMapper mapper;

    public OrganizationChatRepositoryImpl(
            final OperatorFilteredRepository<ChatConversationEntity> conversationRepository,
            final OperatorFilteredRepository<ChatParticipantEntity> participantRepository,
            final OperatorFilteredRepository<ChatMessageEntity> messageRepository,
            final ChatPersistenceMapper mapper) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ChatConversation> findDirectConversation(final DirectChatParticipants participants) {
        return conversationRepository.createCriteria(ChatConversationEntity.class)
                .eq("firstUserId.value", participants.firstUserId().value())
                .eq("secondUserId.value", participants.secondUserId().value())
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ChatConversation> findConversation(final ChatConversationId conversationId) {
        return conversationRepository.createCriteria(ChatConversationEntity.class)
                .eq("id.value", conversationId.value())
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public void createConversation(final ChatConversation conversation) {
        conversationRepository.create(mapper.toEntity(conversation));
        participantRepository.create(new ChatParticipantEntity(conversation.id(),
                conversation.participants().firstUserId(), conversation.createdAt()));
        participantRepository.create(new ChatParticipantEntity(conversation.id(),
                conversation.participants().secondUserId(), conversation.createdAt()));
    }

    @Override
    public void saveConversation(final ChatConversation conversation) {
        conversationRepository.update(mapper.toEntity(conversation));
    }

    @Override
    public void saveMessage(final ChatMessageSnapshot message) {
        messageRepository.create(mapper.toEntity(message));
    }

    @Override
    public Optional<ChatMessageSnapshot> findMessage(final UserId senderUserId, final UUID clientMessageId) {
        return messageRepository.createCriteria(ChatMessageEntity.class)
                .eq("senderUserId.value", senderUserId.value())
                .eq("clientMessageId", clientMessageId)
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public List<ChatMessageSnapshot> findMessages(final ChatConversationId conversationId,
                                                final ChatMessageId beforeMessageId, final int limit) {
        final Criteria<ChatMessageEntity> criteria = messageRepository.createCriteria(ChatMessageEntity.class)
                .eq("conversationId.value", conversationId.value())
                .desc("id.value")
                .maxResults(limit);
        if (beforeMessageId != null) {
            criteria.lt("id.value", beforeMessageId.value());
        }
        final List<ChatMessageEntity> messages = criteria.list();
        return messages.reversed().stream().map(mapper::toDomain).toList();
    }
}
