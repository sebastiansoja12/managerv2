package com.warehouse.redirect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import com.warehouse.redirect.domain.exception.*;
import com.warehouse.redirect.domain.vo.RedirectParcelRequest;
import com.warehouse.redirect.domain.port.secondary.RedirectServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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

    @Mock
    private RedirectServicePort redirectServicePort;

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

        doReturn(true)
                .when(redirectServicePort)
                        .exists(1L);

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

    @Test
    void shouldNotSendInformationWhenParcelDoesNotExist() {
        // given
        final RedirectRequest request = new RedirectRequest();
        request.setEmail("sebastian5152@wp.pl");
        request.setParcelId(1L);

        final RedirectToken redirectToken = new RedirectToken("12345", 1L, "sebastian5152@wp.pl");

        doReturn(false)
                .when(redirectServicePort)
                .exists(1L);

        doReturn("12345")
                .when(redirectTokenGenerator)
                .generateToken(redirectToken.getParcelId(), redirectToken.getEmail());

        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(request);
        // then
        final ParcelRedirectException exception = assertThrows(ParcelRedirectException.class, executable);
        assertEquals(expectedToBe("Parcel to redirect does not exist"), exception.getMessage());
        assertEquals(expectedToBe(501), exception.getCode());
    }

    @Test
    void shouldNotSendInformationWhenRequestIsEmpty() {
        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(null);
        // then
        final RedirectRequestNotFoundException exception = assertThrows(RedirectRequestNotFoundException.class, executable);
        assertEquals(expectedToBe("Redirect request is null"), exception.getMessage());
        assertEquals(expectedToBe(500), exception.getCode());
    }

    @Test
    void shouldNotSendInformationWhenParcelIsNull() {
        // given
        final RedirectRequest request = new RedirectRequest();
        request.setEmail("sebastian5152@wp.pl");
        request.setParcelId(null);
        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(request);
        // then
        final NullParcelIdException exception = assertThrows(NullParcelIdException.class, executable);
        assertEquals(expectedToBe("Parcel id is null"), exception.getMessage());
        assertEquals(expectedToBe(503), exception.getCode());
    }

    @Test
    void shouldNotSendInformationWhenEmailIsNull() {
        // given
        final RedirectRequest request = new RedirectRequest();
        request.setEmail("");
        request.setParcelId(1L);
        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(request);
        // then
        final EmptyEmailException exception = assertThrows(EmptyEmailException.class, executable);
        assertEquals(expectedToBe("Email is empty"), exception.getMessage());
        assertEquals(expectedToBe(502), exception.getCode());
    }

    @Test
    void shouldNotRedirectWhenRequestIsNull() {
        // given && when
        final Executable executable = () -> redirectTokenPort.redirect(null);
        // then
		final RedirectRequestNotFoundException exception = assertThrows(RedirectRequestNotFoundException.class,
				executable);
        assertEquals(expectedToBe("Redirect request is null"), exception.getMessage());
        assertEquals(expectedToBe(500), exception.getCode());
    }

    @Test
    void shouldNotRedirectWhenTokenIsEmpty() {
        // given
        final RedirectParcelRequest request = new RedirectParcelRequest(null, null, null);
        // when
        final Executable executable = () -> redirectTokenPort.redirect(request);
        // then
        final TokenNotFoundException exception = assertThrows(TokenNotFoundException.class, executable);
        assertEquals(expectedToBe("Redirect token is empty"), exception.getMessage());
        assertEquals(expectedToBe(504), exception.getCode());
    }

    @Test
    void shouldNotRedirectWhenParcelIdIsNull() {
        // given
        final RedirectParcelRequest request = new RedirectParcelRequest(null, null, new Token("12345"));
        // when
        final Executable executable = () -> redirectTokenPort.redirect(request);
        // then
        final NullParcelIdException exception = assertThrows(NullParcelIdException.class, executable);
        assertEquals(expectedToBe("Parcel id is null"), exception.getMessage());
        assertEquals(expectedToBe(503), exception.getCode());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
