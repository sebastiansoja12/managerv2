package com.warehouse.mail.domain.vo;

import lombok.*;

@Value
@Builder
public class Notification {
    String subject;
    String recipient;
    String body;
}
