package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.infrastructure.adapter.secondary.api.ChatMessageNotificationDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static com.warehouse.chat.ChatFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ChatNotificationServiceAdapterTest {

    @Test
    void shouldNotifyBothParticipantsWithTheOtherUserAndMessagePayload() {
        final SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        final ChatNotificationServiceAdapter adapter = new ChatNotificationServiceAdapter(template);

        adapter.notifyParticipants(message(10L), PARTICIPANTS);

        final ArgumentCaptor<ChatMessageNotificationDto> first = ArgumentCaptor.forClass(ChatMessageNotificationDto.class);
        final ArgumentCaptor<ChatMessageNotificationDto> second = ArgumentCaptor.forClass(ChatMessageNotificationDto.class);
        verify(template).convertAndSendToUser(eq("1"), eq("/queue/chat/messages"), first.capture());
        verify(template).convertAndSendToUser(eq("2"), eq("/queue/chat/messages"), second.capture());
        assertThat(first.getValue().participantUserId()).isEqualTo(PARTICIPANT_USER_ID);
        assertThat(second.getValue().participantUserId()).isEqualTo(CURRENT_USER_ID);
        assertThat(first.getValue().message().id()).isEqualTo(message(10L).id());
        assertThat(first.getValue().message().body()).isEqualTo("Ready");
        assertThat(second.getValue().message()).isEqualTo(first.getValue().message());
        verifyNoMoreInteractions(template);
    }
}
