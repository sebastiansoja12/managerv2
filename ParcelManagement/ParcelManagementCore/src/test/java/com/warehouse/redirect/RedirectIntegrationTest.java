package com.warehouse.redirect;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.redirect.configuration.RedirectTokenTestConfiguration;
import com.warehouse.redirect.domain.exception.EmptyEmailException;
import com.warehouse.redirect.domain.exception.NullParcelIdException;
import com.warehouse.redirect.domain.exception.ParcelRedirectException;
import com.warehouse.redirect.domain.exception.RedirectRequestNotFoundException;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPort;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.port.secondary.RedirectServicePort;
import com.warehouse.redirect.domain.vo.RedirectToken;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RedirectTokenTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class RedirectIntegrationTest {


    @Autowired
    private RedirectTokenPort redirectTokenPort;

    @Autowired
    private MailServicePort mailServicePort;

    @Autowired
    private RedirectServicePort redirectServicePort;

    public static final String TOKEN = "12345678";

    @Test
    void shouldSendRedirectInformation() {
        // given
        final Long parcelId = 100001L;
        final String email = "recipient@test.pl";
        final RedirectRequest request = new RedirectRequest();
        request.setEmail(email);
        request.setParcelId(parcelId);

        final RedirectToken redirectToken = new RedirectToken(TOKEN, parcelId, email);

        when(redirectServicePort.exists(parcelId)).thenReturn(true);

        doNothing()
                .when(mailServicePort)
                .sendRedirectInformation(redirectToken);

        // when
        final RedirectResponse response = redirectTokenPort.sendRedirectInformation(request);
        // then
        assertEquals(expected(parcelId), response.getParcelId());
        assertEquals(expected(8), response.getToken().getValue().length());
    }

    @Test
    void shouldThrowParcelRedirectException() {
        // given
        final String exceptionMessage = "Parcel to redirect does not exist";
        final Integer exceptionCode = 501;
        final Long parcelId = 100000L;
        final String email = "recipient@test.pl";
        final RedirectRequest request = new RedirectRequest();
        request.setEmail(email);
        request.setParcelId(parcelId);

        when(redirectServicePort.exists(parcelId)).thenReturn(false);

        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(request);
        // then
        final ParcelRedirectException exception = assertThrows(ParcelRedirectException.class, executable);
        assertEquals(expected(exceptionMessage), exception.getMessage());
        assertEquals(expected(exceptionCode), exception.getCode());
    }

    @Test
	void shouldThrowExceptionWhenRedirectRequestIsNull() {
		// given
		final String exceptionMessage = "Redirect request is null";
		final Integer exceptionCode = 500;
		// when
		final Executable executable = () -> redirectTokenPort.sendRedirectInformation(null);
		// then
		final RedirectRequestNotFoundException exception = assertThrows(RedirectRequestNotFoundException.class,
				executable);
		assertEquals(expected(exceptionMessage), exception.getMessage());
		assertEquals(expected(exceptionCode), exception.getCode());
    }

    @Test
    void shouldThrowEmptyEmailExceptionWhenEmailIsNull() {
        // given
        final String exceptionMessage = "Email is empty";
        final Integer exceptionCode = 502;
        final RedirectRequest request = new RedirectRequest();
        request.setParcelId(1L);
        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(request);
        // then
        final EmptyEmailException exception = assertThrows(EmptyEmailException.class, executable);
        assertEquals(expected(exceptionMessage), exception.getMessage());
        assertEquals(expected(exceptionCode), exception.getCode());
    }

    @Test
    void shouldThrowNullParcelIdExceptionWhenParcelIdIsNull() {
        // given
        final String exceptionMessage = "Parcel id is null";
        final Integer exceptionCode = 503;
        final RedirectRequest request = new RedirectRequest();
        request.setEmail("email");
        // when
        final Executable executable = () -> redirectTokenPort.sendRedirectInformation(request);
        // then
        final NullParcelIdException exception = assertThrows(NullParcelIdException.class, executable);
        assertEquals(expected(exceptionMessage), exception.getMessage());
        assertEquals(expected(exceptionCode), exception.getCode());
    }

    private <T> T expected(T t) {
        return t;
    }
}
