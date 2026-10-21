package com.warehouse.mail.infrastructure.adapter.primary.event;

import java.util.Map;

public record MailTemplateNotificationEvent(String templateKey,
                                            String recipientEmail,
                                            String recipientCountryCode,
                                            Map<String, String> variables) {
}
