package com.warehouse.redirect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.redirect.domain.exception.EmptyEmailException;
import com.warehouse.redirect.domain.exception.NullParcelIdException;
import com.warehouse.redirect.domain.exception.RedirectRequestNotFoundException;
import com.warehouse.redirect.domain.exception.TokenNotFoundException;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPortImpl;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.port.secondary.RedirectServicePort;
import com.warehouse.redirect.domain.port.secondary.RedirectTrackerServicePort;
import com.warehouse.redirect.domain.service.RedirectService;
import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import com.warehouse.redirect.domain.vo.RedirectParcelRequest;
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

    @Mock
    private RedirectTrackerServicePort redirectTrackerServicePort;

    private RedirectTokenPortImpl redirectTokenPort;

    @BeforeEach
    void setup() {
        redirectTokenPort = new RedirectTokenPortImpl(redirectService, redirectTokenGenerator, mailServicePort,
                redirectTrackerServicePort);
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
