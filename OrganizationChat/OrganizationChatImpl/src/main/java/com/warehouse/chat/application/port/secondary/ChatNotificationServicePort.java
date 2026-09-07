package com.warehouse.chat.application.port.secondary;

import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;

public interface ChatNotificationServicePort {

    void notifyParticipants(final ChatMessageSnapshot message, final DirectChatParticipants participants);
}
