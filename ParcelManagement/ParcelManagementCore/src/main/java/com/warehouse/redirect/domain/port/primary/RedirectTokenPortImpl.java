package com.warehouse.redirect.domain.port.primary;

import com.warehouse.redirect.domain.exception.EmptyEmailException;
import com.warehouse.redirect.domain.exception.NullParcelIdException;
import com.warehouse.redirect.domain.exception.ParcelRedirectException;
import com.warehouse.redirect.domain.exception.RedirectRequestNotFoundException;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.port.secondary.RedirectServicePort;
import com.warehouse.redirect.domain.service.RedirectService;
import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor

public class RedirectTokenPortImpl implements RedirectTokenPort {

    private final RedirectService redirectService;

	private final RedirectTokenGenerator redirectTokenGenerator;

	private final MailServicePort mailServicePort;

	private final RedirectServicePort redirectServicePort;

	private final Logger logger = LoggerFactory.getLogger(RedirectTokenPort.class);


    @Override
    public RedirectResponse sendRedirectInformation(RedirectRequest request) {
        handleRequest(request);
		final RedirectToken redirectToken = buildRedirectTokenFromRequest(request);

		logRedirectToken(redirectToken);

		final boolean exists = redirectServicePort.exists(request.getParcelId());

		if (!exists) {
			throw new ParcelRedirectException(501, "Parcel to redirect does not exist");
		}

		final Token token = redirectService.saveRedirectToken(redirectToken);


		mailServicePort.sendRedirectInformation(redirectToken);

        return RedirectResponse.builder()
				.parcelId(request.getParcelId())
				.token(token)
				.build();
    }

	private void logRedirectToken(RedirectToken redirectToken) {
		logger.info("Request for redirecting parcel {} has been recorded", redirectToken.getParcelId());
	}

	private RedirectToken buildRedirectTokenFromRequest(RedirectRequest request) {
		final String token = redirectTokenGenerator.generateToken(request.getParcelId(), request.getEmail());
		return new RedirectToken(token, request.getParcelId(), request.getEmail());
	}

	private void handleRequest(RedirectRequest request) { 
		if (request == null) {
			throw new RedirectRequestNotFoundException(500, "Redirect request is null");
		} else if (request.getEmail().isEmpty()) {
			throw new EmptyEmailException(502, "Email is empty");
		} else if (request.getParcelId() == null) {
			throw new NullParcelIdException(503, "Parcel id is null");
		}
	}
}
