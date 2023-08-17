package com.warehouse.redirect.domain.port.primary;

import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.model.RedirectResponse;

import com.warehouse.redirect.domain.model.RedirectToken;
import com.warehouse.redirect.domain.service.RedirectService;
import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectTokenPortImpl implements RedirectTokenPort {

    private final RedirectService redirectService;

	private final RedirectTokenGenerator redirectTokenGenerator;

    @Override
    public RedirectResponse sendRedirectInformation(RedirectRequest request) {
        handleRequest(request);
		final RedirectToken redirectToken = buildRedirectTokenFromRequest(request);

        return RedirectResponse.builder()
				.parcelId(request.getParcelId())
				.token(redirectToken.getToken())
				.build();
    }

	private RedirectToken buildRedirectTokenFromRequest(RedirectRequest request) {
		return RedirectToken.builder()
				.token(redirectTokenGenerator.generateToken(request.getParcelId(), request.getEmail()))
				.email(request.getEmail())
				.parcelId(request.getParcelId())
				.build();
	}

	private void handleRequest(RedirectRequest request) {
		if (request == null) {
			throw new RuntimeException("");
		} else if (request.getEmail().isEmpty()) {
			throw new RuntimeException("");
		} else if (request.getParcelId() == null) {
			throw new RuntimeException("");
		}
	}
}
