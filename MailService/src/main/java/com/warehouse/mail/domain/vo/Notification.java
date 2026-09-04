package com.warehouse.mail.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Notification {
    String subject;
    String recipient;
    String body;
}
