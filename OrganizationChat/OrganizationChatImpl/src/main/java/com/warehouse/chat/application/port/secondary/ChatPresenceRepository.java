package com.warehouse.chat.application.port.secondary;

import com.warehouse.chat.domain.vo.ChatPresence;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public interface ChatPresenceRepository {

    void connect(final ChatPresence presence);

    Optional<ChatPresence> disconnect(final String sessionId);

    List<UserId> findOnlineUsers(final OperatorId operatorId);
}
