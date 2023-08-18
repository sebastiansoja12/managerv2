package com.warehouse.redirect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPortImpl;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.service.RedirectService;
import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;

@ExtendWith(MockitoExtension.class)
public class RedirectTokenPortImplTest {

    @Mock
    private RedirectService redirectService;

    @Mock
    private RedirectTokenGenerator redirectTokenGenerator;

    @Mock
    private MailServicePort mailServicePort;

    private RedirectTokenPortImpl redirectTokenPort;

    @BeforeEach
    void setup() {
        redirectTokenPort = new RedirectTokenPortImpl(redirectService, redirectTokenGenerator, mailServicePort);
    }

    @Test
    void shouldSendInformation() {
        // given
        final RedirectRequest request = new RedirectRequest();
        request.setEmail("sebastian5152@wp.pl");
        request.setParcelId(1L);

        final RedirectToken redirectToken = new RedirectToken("12345", 1L, "sebastian5152@wp.pl");
        final Token token = new Token("12345");

        doReturn("12345")
                .when(redirectTokenGenerator)
                        .generateToken(redirectToken.getParcelId(), redirectToken.getEmail());

        doReturn(token)
                .when(redirectService)
                        .saveRedirectToken(redirectToken);

        doNothing()
                .when(mailServicePort)
                    .sendRedirectInformation(redirectToken);
        // when
        final RedirectResponse response = redirectTokenPort.sendRedirectInformation(request);
        // then
        assertEquals(expectedToBe(token), response.getToken());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
