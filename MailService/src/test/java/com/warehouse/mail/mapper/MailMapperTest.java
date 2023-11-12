package com.warehouse.mail.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mapstruct.factory.Mappers.getMapper;

import org.junit.jupiter.api.Test;

import com.warehouse.mail.domain.model.RerouteNotification;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.mail.infrastructure.adapter.secondary.mapper.MailMapper;

public class MailMapperTest {

    private final MailMapper mapper = getMapper(MailMapper.class);


    @Test
    void shouldMap() {
        // given
        final Notification notification = Notification.builder()
                .body("Test Body")
                .recipient("test@example.com")
                .subject("Test Subject")
                .build();
        // when
        final RerouteNotification rerouteNotification = mapper.map(notification);
        // then
		assertAll(
                () -> assertEquals(notification.getBody(), rerouteNotification.getBody()),
				() -> assertEquals(notification.getSubject(), rerouteNotification.getSubject()),
				() -> assertEquals(notification.getRecipient(), rerouteNotification.getRecipient())
        );
    }
}
