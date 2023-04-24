package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteNotification;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RequestMapper;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RerouteTokenAdapterTest {

    public static final String EMAIL = "test.pl";

    @Mock
    private MailService mailService;

    @Mock
    private RerouteTokenRepository rerouteTokenRepository;

    @Mock
    private ResponseMapper responseMapper;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RerouteTokenAdapter rerouteTokenAdapter;

    private final static Long PARCEL_ID = 100001L;

    private final static Integer TOKEN = 12345;


    @Test
    void shouldSendReroutingInformation() {
        // given
        final RerouteRequest rerouteRequest = RerouteRequest.builder()
                .email(EMAIL)
                .parcelId(PARCEL_ID)
                .build();

        final RerouteResponse rerouteResponse = RerouteResponse.builder()
                .token(TOKEN)
                .parcelId(PARCEL_ID)
                .build();
        final Notification notification = new Notification();
        notification.setBody("test");
        notification.setRecipient(EMAIL);
        notification.setSubject(PARCEL_ID.toString());

        final RerouteNotification rerouteNotification = buildNotification(rerouteRequest, rerouteResponse);

        when(rerouteTokenRepository.saveReroutingToken(PARCEL_ID)).thenReturn(TOKEN);
        when(responseMapper.map(rerouteRequest, TOKEN)).thenReturn(rerouteResponse);
        when(requestMapper.map(rerouteNotification)).thenReturn(notification);
        // when
        final RerouteResponse actualResponse = rerouteTokenAdapter.sendReroutingInformation(rerouteRequest);

        // then
        assertAll(
                () -> assertThat(actualResponse.getToken().intValue()).isEqualTo(TOKEN),
                () -> assertThat(actualResponse.getParcelId()).isEqualTo(PARCEL_ID)

        );
    }

    public RerouteNotification buildNotification(RerouteRequest rerouteRequest, RerouteResponse rerouteResponse) {
        return RerouteNotification.builder()
                .body("Prosimy wejść w link: " + "http://localhost:4200/reroute-edit/" + rerouteRequest.getParcelId()
                        + "/" + rerouteResponse.getToken())
                .recipient(rerouteRequest.getEmail())
                .subject("Zmiana danych przesyłki: " + rerouteRequest.getParcelId())
                .build();
    }
}
