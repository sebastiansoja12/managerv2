package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Controller
public class OrganizationChatPresenceWebSocketAdapter {

    private final OrganizationChatPort chatPort;

    public OrganizationChatPresenceWebSocketAdapter(final OrganizationChatPort chatPort) {
        this.chatPort = chatPort;
    }

    @EventListener
    public void onDisconnected(final SessionDisconnectEvent event) {
        chatPort.disconnectPresence(event.getSessionId());
    }

    @MessageMapping("/chat/presence/online")
    public void online(@Header("simpSessionId") final String sessionId, final Principal principal) {
        connect(sessionId, principal);
    }

    @MessageMapping("/chat/presence/offline")
    public void offline(@Header("simpSessionId") final String sessionId, final Principal principal) {
        if (principal instanceof UserIdPrincipal) {
            chatPort.disconnectPresence(sessionId);
        }
    }

    private void connect(final String sessionId, final Principal principal) {
        if (sessionId != null && principal instanceof UserIdPrincipal userIdPrincipal) {
            chatPort.connectPresence(sessionId, userIdPrincipal.userId());
        }
    }

    public interface UserIdPrincipal extends Principal {

        UserId userId();
    }
}
