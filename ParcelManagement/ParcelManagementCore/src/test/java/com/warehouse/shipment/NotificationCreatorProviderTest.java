package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.Recipient;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.NotificationCreatorProviderImpl;
import com.warehouse.shipment.domain.model.Notification;

public class NotificationCreatorProviderTest {

    private final NotificationCreatorProvider notificationCreatorProvider =
            new NotificationCreatorProviderImpl();

    @Test
    void shouldBuildNotification() {
        // given
        final String subject = "Została nadana do państwa przesyłka: ";
        final Recipient recipient = Recipient.builder()
                .email("test@test.pl")
                .build();
        final Parcel parcel = Parcel.builder()
                .recipient(recipient)
                .build();
        final String payment = "paymentUrl";
        // when
        final Notification notification = notificationCreatorProvider.createNotification(parcel, payment);
        // then
        assertAll(
                () -> assertEquals(subject, notification.getSubject()),
                () -> assertEquals(recipient.getEmail(), notification.getRecipient())
        );
    }
}
