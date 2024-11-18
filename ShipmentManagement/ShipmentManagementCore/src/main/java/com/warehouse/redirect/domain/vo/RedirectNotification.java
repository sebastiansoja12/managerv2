package com.warehouse.redirect.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RedirectNotification {
     String subject;
     String recipient;
     String body;
}
