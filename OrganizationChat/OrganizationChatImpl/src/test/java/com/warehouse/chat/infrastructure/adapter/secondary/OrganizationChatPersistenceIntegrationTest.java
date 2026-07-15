package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.chat.OrganizationChatApiService;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.chat.application.port.primary.command.SendChatMessageCommand;
import com.warehouse.chat.application.port.secondary.OrganizationChatRepository;
import com.warehouse.chat.configuration.OrganizationChatConfiguration;
import com.warehouse.chat.domain.model.ChatConversation;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.infrastructure.adapter.secondary.api.ChatMessageNotificationDto;
import com.warehouse.commonassets.event.infrastructure.adapter.secondary.SpringDomainEventPublisher;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.Optional;
import java.util.UUID;

import static com.warehouse.chat.ChatFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = OrganizationChatPersistenceIntegrationTest.JpaTestConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrganizationChatPersistenceIntegrationTest {

    private final OrganizationChatRepository repository;
    private final OperatorFilteredRepository<ChatParticipantEntity> participantRepository;
    private final OperatorContextProvider operatorContextProvider;
    private final TestEntityManager entityManager;
    private final OrganizationChatPort chatPort;
    private final OrganizationChatApiService apiService;
    private final SimpMessagingTemplate template;

    OrganizationChatPersistenceIntegrationTest(final OrganizationChatRepository repository,
                                               @Qualifier("chatParticipantBaseRepository")
                                               final OperatorFilteredRepository<ChatParticipantEntity> participantRepository,
                                               final OperatorContextProvider operatorContextProvider,
                                               final TestEntityManager entityManager,
                                               final OrganizationChatPort chatPort,
                                               final OrganizationChatApiService apiService,
                                               final SimpMessagingTemplate template) {
        this.repository = repository;
        this.participantRepository = participantRepository;
        this.operatorContextProvider = operatorContextProvider;
        this.entityManager = entityManager;
        this.chatPort = chatPort;
        this.apiService = apiService;
        this.template = template;
        reset(template);
    }

    @Test
    void shouldPersistAndRestoreAggregateAndBothParticipantsWithStableIdentifiers() {
        final ChatConversation conversation = conversation();
        repository.createConversation(conversation);
        final ChatMessageSnapshot message = conversation.sendMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID, "Ready", NOW);
        repository.saveMessage(message);
        repository.saveConversation(conversation);

        entityManager.flush();
        entityManager.clear();

        final ChatConversation restored = repository.findDirectConversation(PARTICIPANTS).orElseThrow();
        assertThat(restored.id()).isEqualTo(CONVERSATION_ID);
        assertThat(restored.participants()).isEqualTo(PARTICIPANTS);
        assertThat(restored.lastMessageAt()).isEqualTo(NOW);
        assertThat(participantExists(CURRENT_USER_ID)).isTrue();
        assertThat(participantExists(PARTICIPANT_USER_ID)).isTrue();
        assertThat(repository.findMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID)).contains(message);
        when(operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OperatorId.of(99L)));
        assertThat(repository.findConversation(CONVERSATION_ID)).isEmpty();
        assertThat(repository.findMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID)).isEmpty();
        assertThat(participantExists(CURRENT_USER_ID)).isFalse();
        when(operatorContextProvider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
    }

    @Test
    void shouldReturnLatestPageChronologicallyAndHonorExclusiveCursor() {
        repository.createConversation(conversation());
        for (long id = 10; id <= 12; id++) {
            repository.saveMessage(new ChatMessageSnapshot(ChatMessageId.of(id), CONVERSATION_ID, CURRENT_USER_ID,
                    UUID.randomUUID(), "Message " + id, NOW));
        }
        entityManager.flush();
        entityManager.clear();

        assertThat(repository.findMessages(CONVERSATION_ID, null, 2)).extracting(message -> message.id().value())
                .containsExactly(11L, 12L);
        assertThat(repository.findMessages(CONVERSATION_ID, ChatMessageId.of(12L), 2))
                .extracting(message -> message.id().value()).containsExactly(10L, 11L);
    }

    @Test
    void shouldNotifyOnlyAfterCommitAndExposeSavedMessageThroughInternalApi() {
        repository.createConversation(conversation());
        final ChatMessageSnapshot message = chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready"));

        verifyNoInteractions(template);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        verify(template).convertAndSendToUser(eq("1"), eq("/queue/chat/messages"), any(ChatMessageNotificationDto.class));
        verify(template).convertAndSendToUser(eq("2"), eq("/queue/chat/messages"), any(ChatMessageNotificationDto.class));
        assertThat(apiService.findMessages(CONVERSATION_ID, null, 50)).extracting(saved -> saved.id())
                .containsExactly(message.id());
        TestTransaction.start();
        entityManager.getEntityManager().createQuery("delete from ChatMessageEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ChatParticipantEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ChatConversationEntity").executeUpdate();
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    @Test
    void shouldRollbackMessageAndSuppressNotifications() {
        repository.createConversation(conversation());
        chatPort.sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready"));

        TestTransaction.flagForRollback();
        TestTransaction.end();

        verifyNoInteractions(template);
        assertThat(repository.findMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID)).isEmpty();
    }

    @EnableJpaRepositories(basePackages = "com.warehouse.chat.infrastructure.adapter.secondary")
    @EntityScan(basePackageClasses = ChatConversationEntity.class)
    @Import({OrganizationChatConfiguration.class, SpringDomainEventPublisher.class})
    static class JpaTestConfiguration {

        @Bean
        CurrentUserApiService currentUserApiService() {
            final CurrentUserApiService service = mock(CurrentUserApiService.class);
            when(service.getCurrentUserId()).thenReturn(CURRENT_USER_ID);
            return service;
        }

        @Bean
        CurrentOperatorService currentOperatorService() {
            final CurrentOperatorService service = mock(CurrentOperatorService.class);
            when(service.getCurrentOperatorId()).thenReturn(OPERATOR_ID);
            return service;
        }

        @Bean
        OperatorContextProvider operatorContextProvider() {
            final OperatorContextProvider provider = mock(OperatorContextProvider.class);
            when(provider.currentOperatorId()).thenReturn(Optional.of(OPERATOR_ID));
            return provider;
        }

        @Bean
        UserApiService userApiService() {
            return mock(UserApiService.class);
        }

        @Bean
        SimpMessagingTemplate messagingTemplate() {
            return mock(SimpMessagingTemplate.class);
        }
    }

    private boolean participantExists(final UserId userId) {
        return participantRepository.createCriteria(ChatParticipantEntity.class)
                .eq("conversationId.value", CONVERSATION_ID.value())
                .eq("userId.value", userId.value())
                .one()
                .isPresent();
    }
}
