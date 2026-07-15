package com.warehouse.chat.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public interface ChatPresenceNotificationServicePort {

    void notifyUsers(final List<UserId> recipients, final List<UserId> onlineUsers);
}
