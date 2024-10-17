package com.warehouse.redirect.domain.port.primary;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.redirect.domain.exception.EmptyEmailException;
import com.warehouse.redirect.domain.exception.NullParcelIdException;
import com.warehouse.redirect.domain.exception.RedirectRequestNotFoundException;
import com.warehouse.redirect.domain.exception.TokenNotFoundException;
import com.warehouse.redirect.domain.model.RedirectParcelResponse;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.service.RedirectService;
import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import com.warehouse.redirect.domain.vo.RedirectParcelRequest;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectTokenPortImpl implements RedirectTokenPort {

    private final RedirectService redirectService;

	private final RedirectTokenGenerator redirectTokenGenerator;

	private final MailServicePort mailServicePort;

	private final Logger logger = LoggerFactory.getLogger(RedirectTokenPort.class);


    @Override
    public RedirectResponse sendRedirectInformation(RedirectRequest request) {
        handleRequest(request);
		final RedirectToken redirectToken = buildRedirectTokenFromRequest(request);

		logRedirectToken(redirectToken);

		final Token token = redirectService.saveRedirectToken(redirectToken);


		mailServicePort.sendRedirectInformation(redirectToken);

        return RedirectResponse.builder()
				.parcelId(request.getParcelId())
				.token(token)
				.build();
    }

	@Override
	public RedirectParcelResponse redirect(RedirectParcelRequest request) {
		handleRequest(request);

		logRedirectParcelRequest(request);

		return new RedirectParcelResponse();
	}

	private RedirectToken buildRedirectTokenFromRequest(RedirectRequest request) {
		final String token = redirectTokenGenerator.generateToken(request.getParcelId(), request.getEmail());
		return new RedirectToken(token, request.getParcelId(), request.getEmail());
	}

	private void logRedirectParcelRequest(RedirectParcelRequest request) {
		logger.info("Request to redirect parcel {} has been recorded", request.getParcelId());
	}

	private void logRedirectToken(RedirectToken redirectToken) {
		logger.info("Request for redirecting parcel {} has been recorded", redirectToken.getParcelId());
	}

	private void handleRequest(RedirectRequest request) { 
		if (request == null) {
			throw new RedirectRequestNotFoundException(500, "Redirect request is null");
		} else if (StringUtils.isEmpty(request.getEmail())) {
			throw new EmptyEmailException(502, "Email is empty");
		} else if (ObjectUtils.isEmpty(request.getParcelId())) {
			throw new NullParcelIdException(503, "Parcel id is null");
		}
	}

	private void handleRequest(RedirectParcelRequest request) {
		if (request == null) {
			throw new RedirectRequestNotFoundException(500, "Redirect request is null");
		} else if (request.getToken() == null) {
			throw new TokenNotFoundException(504, "Redirect token is empty");
		} else if (request.getParcelId() == null) {
			throw new NullParcelIdException(503, "Parcel id is null");
		}
	}
}
