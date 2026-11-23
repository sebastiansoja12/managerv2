package com.warehouse.chat.domain.event;

import com.warehouse.chat.domain.vo.ChatMessageSnapshot;
import com.warehouse.chat.domain.vo.DirectChatParticipants;
import com.warehouse.eventoutbox.domain.model.DomainEvent;

import java.time.Instant;

public record OrganizationChatMessageCreatedEvent(ChatMessageSnapshot message,
                                                  DirectChatParticipants participants,
                                                  Instant timestamp) implements DomainEvent {

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
