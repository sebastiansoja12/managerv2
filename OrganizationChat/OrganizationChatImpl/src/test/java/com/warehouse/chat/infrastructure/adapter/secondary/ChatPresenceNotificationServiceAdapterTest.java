package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.infrastructure.adapter.secondary.api.ChatPresenceNotificationDto;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ChatPresenceNotificationServiceAdapterTest {

    @Test
    void shouldPushTheCurrentOnlineUsersToEveryConnectedUser() {
        final SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        final ChatPresenceNotificationServiceAdapter adapter = new ChatPresenceNotificationServiceAdapter(template);
        final List<UserId> users = List.of(new UserId(1L), new UserId(2L));

        adapter.notifyUsers(users, users);

        final ArgumentCaptor<ChatPresenceNotificationDto> notification =
                ArgumentCaptor.forClass(ChatPresenceNotificationDto.class);
        verify(template).convertAndSendToUser(eq("1"), eq("/queue/chat/presence"), notification.capture());
        verify(template).convertAndSendToUser(eq("2"), eq("/queue/chat/presence"), notification.capture());
        assertThat(notification.getAllValues())
                .extracting(ChatPresenceNotificationDto::onlineUserIds)
                .containsExactly(List.of("1", "2"), List.of("1", "2"));
        verifyNoMoreInteractions(template);
    }
}
