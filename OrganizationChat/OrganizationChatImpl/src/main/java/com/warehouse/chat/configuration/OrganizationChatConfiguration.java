package com.warehouse.chat.configuration;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.chat.OrganizationChatApiService;
import com.warehouse.chat.application.listener.OrganizationChatMessageCreatedListener;
import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.chat.application.port.primary.OrganizationChatPortImpl;
import com.warehouse.chat.application.port.secondary.ChatNotificationServicePort;
import com.warehouse.chat.application.port.secondary.ChatPresenceNotificationServicePort;
import com.warehouse.chat.application.port.secondary.ChatPresenceRepository;
import com.warehouse.chat.application.port.secondary.ChatUserServicePort;
import com.warehouse.chat.application.port.secondary.OrganizationChatRepository;
import com.warehouse.chat.infrastructure.adapter.primary.OrganizationChatApiServiceAdapter;
import com.warehouse.chat.infrastructure.adapter.primary.mapper.ChatResponseMapper;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatConversationEntity;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatMessageEntity;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatNotificationServiceAdapter;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatParticipantEntity;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatPresenceNotificationServiceAdapter;
import com.warehouse.chat.infrastructure.adapter.secondary.ChatUserServiceAdapter;
import com.warehouse.chat.infrastructure.adapter.secondary.InMemoryChatPresenceStore;
import com.warehouse.chat.infrastructure.adapter.secondary.OrganizationChatRepositoryImpl;
import com.warehouse.chat.infrastructure.adapter.secondary.mapper.ChatPersistenceMapper;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Clock;

@Configuration
public class OrganizationChatConfiguration {

    @Bean
    public OrganizationChatPort organizationChatPort(final OrganizationChatRepository repository,
                                                     final ChatUserServicePort userServicePort,
                                                     final DomainEventPublisher eventPublisher,
                                                     final ChatPresenceRepository presenceRepository,
                                                     final ChatPresenceNotificationServicePort presenceNotificationServicePort) {
        return new OrganizationChatPortImpl(repository, userServicePort, eventPublisher, Clock.systemUTC(),
                presenceRepository, presenceNotificationServicePort);
    }

    @Bean
    public ChatPresenceRepository chatPresenceRepository() {
        return new InMemoryChatPresenceStore();
    }

    @Bean
    public ChatPresenceNotificationServicePort chatPresenceNotificationServicePort(
            final SimpMessagingTemplate messagingTemplate) {
        return new ChatPresenceNotificationServiceAdapter(messagingTemplate);
    }

    @Bean
    public OrganizationChatRepository organizationChatRepository(
            @Qualifier("chatConversationBaseRepository")
            final OperatorFilteredRepository<ChatConversationEntity> conversationRepository,
            @Qualifier("chatParticipantBaseRepository")
            final OperatorFilteredRepository<ChatParticipantEntity> participantRepository,
            @Qualifier("chatMessageBaseRepository")
            final OperatorFilteredRepository<ChatMessageEntity> messageRepository) {
        return new OrganizationChatRepositoryImpl(conversationRepository, participantRepository, messageRepository,
                new ChatPersistenceMapper());
    }

    @Bean("chatConversationBaseRepository")
    public BaseRepository<ChatConversationEntity> chatConversationBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("chatParticipantBaseRepository")
    public BaseRepository<ChatParticipantEntity> chatParticipantBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("chatMessageBaseRepository")
    public BaseRepository<ChatMessageEntity> chatMessageBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public ChatUserServicePort chatUserServicePort(final CurrentUserApiService currentUserApiService,
                                                  final CurrentOperatorService currentOperatorService,
                                                  final UserApiService userApiService) {
        return new ChatUserServiceAdapter(currentUserApiService, currentOperatorService, userApiService);
    }

    @Bean
    public ChatResponseMapper chatResponseMapper() {
        return new ChatResponseMapper();
    }

    @Bean
    public OrganizationChatApiService organizationChatApiService(final OrganizationChatPort chatPort,
                                                                final ChatResponseMapper mapper) {
        return new OrganizationChatApiServiceAdapter(chatPort, mapper);
    }

    @Bean
    public ChatNotificationServicePort chatNotificationServicePort(final SimpMessagingTemplate messagingTemplate) {
        return new ChatNotificationServiceAdapter(messagingTemplate);
    }

    @Bean
    public OrganizationChatMessageCreatedListener organizationChatMessageCreatedListener(
            final ChatNotificationServicePort notificationServicePort) {
        return new OrganizationChatMessageCreatedListener(notificationServicePort);
    }
}
