package com.warehouse.deliveryreturn;

import com.warehouse.deliveryreturn.domain.vo.Parcel;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.tools.mail.MailProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailAdapterTest {

    @Mock
    private MailProperty mailProperty;

    @Mock
    private MailPort mailPort;

    @InjectMocks
    private MailAdapter mailAdapter;

    @Test
    void shouldSendNotification() {
        // given
        final Parcel parcel = Parcel.builder()
                .senderEmail("email")
                .id(1L)
                .parcelStatus("RETURN")
                .recipientEmail("email")
                .build();
        doReturn("Test message")
                .when(mailProperty)
                .getMessage();
        doReturn("Subject")
                .when(mailProperty)
                .getSubject();
        // when
        mailAdapter.sendNotification(parcel);
        // then
        verify(mailPort).sendNotification(any());
    }

}
