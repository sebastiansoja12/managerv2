package com.warehouse.chat.configuration;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class OrganizationChatIdentifierWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, UserId.class,
                source -> new UserId(parsePositiveIdentifier(source, "userId")));
        registry.addConverter(String.class, ChatConversationId.class,
                source -> ChatConversationId.of(parsePositiveIdentifier(source, "conversationId")));
        registry.addConverter(String.class, ChatMessageId.class,
                source -> ChatMessageId.of(parsePositiveIdentifier(source, "messageId")));
    }

    private Long parsePositiveIdentifier(final String source, final String name) {
        final long value = Long.parseLong(source);
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return value;
    }
}
