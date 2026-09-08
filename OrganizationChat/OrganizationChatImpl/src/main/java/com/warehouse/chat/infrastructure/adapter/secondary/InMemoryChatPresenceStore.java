package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.chat.application.port.secondary.ChatPresenceRepository;
import com.warehouse.chat.domain.vo.ChatPresence;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryChatPresenceStore implements ChatPresenceRepository {

    private final Map<String, ChatPresence> sessions = new ConcurrentHashMap<>();

    @Override
    public void connect(final ChatPresence presence) {
        sessions.put(presence.sessionId(), presence);
    }

    @Override
    public Optional<ChatPresence> disconnect(final String sessionId) {
        return Optional.ofNullable(sessions.remove(sessionId));
    }

    @Override
    public List<UserId> findOnlineUsers(final OperatorId operatorId) {
        return sessions.values().stream()
                .filter(presence -> operatorId.equals(presence.operatorId()))
                .map(ChatPresence::userId)
                .distinct()
                .toList();
    }
}
