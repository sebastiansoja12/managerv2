package com.warehouse.chat.domain.model;

import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.ChatRecipient;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.warehouse.chat.ChatFixture.*;
import static com.warehouse.chat.domain.exception.ChatException.Reason.*;
import static org.assertj.core.api.Assertions.*;

class ChatConversationTest {

    @Test
    void shouldNormalizeParticipantOrder() {
        final DirectChatParticipants participants = new DirectChatParticipants(PARTICIPANT_USER_ID, CURRENT_USER_ID);

        assertThat(participants).isEqualTo(PARTICIPANTS);
        assertThat(participants.otherUser(CURRENT_USER_ID)).isEqualTo(PARTICIPANT_USER_ID);
    }

    @Test
    void shouldRejectConversationWithSelf() {
        assertThatThrownBy(() -> new DirectChatParticipants(CURRENT_USER_ID, CURRENT_USER_ID))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", INVALID_REQUEST);
    }

    @Test
    void shouldRejectNonParticipantWithoutChangingConversation() {
        final ChatConversation conversation = conversation();

        assertThatThrownBy(() -> conversation.sendMessage(new UserId(3L), CLIENT_MESSAGE_ID, "Ready", NOW))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);
        assertThat(conversation.lastMessageAt()).isNull();
    }

    @Test
    void shouldCreateMessageAndUpdateConversation() {
        final ChatConversation conversation = conversation();

        final ChatMessageSnapshot message = conversation.sendMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID, "  Ready  ", NOW);

        assertThat(message.id().value()).isPositive();
        assertThat(message.conversationId()).isEqualTo(CONVERSATION_ID);
        assertThat(message.body()).isEqualTo("Ready");
        assertThat(message.sentAt()).isEqualTo(NOW);
        assertThat(conversation.lastMessageAt()).isEqualTo(NOW);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void shouldRejectBlankMessageWithoutChangingConversation(final String body) {
        final ChatConversation conversation = conversation();

        assertThatThrownBy(() -> conversation.sendMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID, body, NOW))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", INVALID_REQUEST);
        assertThat(conversation.lastMessageAt()).isNull();
    }

    @Test
    void shouldEnforceMessageLength() {
        final ChatConversation conversation = conversation();

        assertThatThrownBy(() -> conversation.sendMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID, "x".repeat(2001), NOW))
                .isInstanceOf(ChatException.class);
        assertThat(conversation.lastMessageAt()).isNull();
        assertThat(conversation.sendMessage(CURRENT_USER_ID, CLIENT_MESSAGE_ID, "x".repeat(2000), NOW).body()).hasSize(2000);
    }

    @Test
    void shouldRequireClientMessageIdentifier() {
        assertThatThrownBy(() -> conversation().sendMessage(CURRENT_USER_ID, null, "Ready", NOW))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", INVALID_REQUEST);
    }

    @Test
    void shouldRejectInactiveAndForeignRecipients() {
        assertThatThrownBy(() -> new ChatRecipient(OPERATOR_ID, false).requireAvailableTo(OPERATOR_ID))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", NOT_FOUND);
        assertThatThrownBy(() -> new ChatRecipient(OperatorId.of(99L), true).requireAvailableTo(OPERATOR_ID))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);
        assertThatThrownBy(() -> new ChatRecipient(null, true).requireAvailableTo(OPERATOR_ID))
                .isInstanceOf(ChatException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);
    }
}
