package com.warehouse.shipment.mapper;

import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationMapperTest {

    private NotificationMapper mapper;


    @BeforeEach
    void setup() {
        mapper = new NotificationMapperImpl();
    }


    @Test
    void shouldMapFromNotificationToMailNotification() {
        // given
        final Notification notification = Notification.builder()
                .body("body")
                .recipient("recipient")
                .subject("subject")
                .build();
        // when
        final com.warehouse.mail.domain.vo.Notification mail = mapper.map(notification);
        // then
        assertThat(mail.getBody()).isEqualTo("body");
    }
}
