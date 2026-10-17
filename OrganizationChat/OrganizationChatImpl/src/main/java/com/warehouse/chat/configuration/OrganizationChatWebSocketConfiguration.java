package com.warehouse.chat.configuration;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.chat.infrastructure.adapter.primary.OrganizationChatPresenceWebSocketAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
class OrganizationChatWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final String[] allowedOrigins;

    OrganizationChatWebSocketConfiguration(
            @Value("${auth.cors.allowed-origins:http://localhost:3000,https://managerv2gui.onrender.com, https://neighbors-billing-ownership-houston.trycloudflare.com/}")
            final String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/ws")
                .setAllowedOrigins(allowedOrigins)
                .addInterceptors(new AuthenticatedChatHandshakeInterceptor())
                .setHandshakeHandler(new UserIdHandshakeHandler());
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/app");
    }

    private static final class AuthenticatedChatHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(final ServerHttpRequest request,
                                       final ServerHttpResponse response,
                                       final WebSocketHandler wsHandler,
                                       final Map<String, Object> attributes) {
            return request.getPrincipal() instanceof Authentication authentication
                    && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof UserId;
        }

        @Override
        public void afterHandshake(final ServerHttpRequest request,
                                   final ServerHttpResponse response,
                                   final WebSocketHandler wsHandler,
                                   final Exception exception) {
        }
    }

    private static final class UserIdHandshakeHandler extends DefaultHandshakeHandler {

        @Override
        protected Principal determineUser(final ServerHttpRequest request,
                                          final WebSocketHandler wsHandler,
                                          final Map<String, Object> attributes) {
            if (request.getPrincipal() instanceof Authentication authentication
                    && authentication.getPrincipal() instanceof UserId userId) {
                return new OrganizationChatPrincipal(userId);
            }
            return null;
        }
    }

    private record OrganizationChatPrincipal(UserId userId)
            implements OrganizationChatPresenceWebSocketAdapter.UserIdPrincipal {

        @Override
        public String getName() {
            return String.valueOf(userId.value());
        }
    }
}
