package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpHeaders;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

public class GenericFeignClientFactory {

	private final CurrentUserApiService currentUserApiService;

	private final Decoder decoder;

	private final Encoder encoder;

	public GenericFeignClientFactory(final ObjectFactory<HttpMessageConverters> messageConverters,
									 final CurrentUserApiService currentUserApiService) {
		this.currentUserApiService = currentUserApiService;
		this.decoder = new ResponseEntityDecoder(new SpringDecoder(messageConverters));
		this.encoder = new SpringEncoder(messageConverters);
	}

	public <T> T create(final Class<T> clientType, final String url) {
		return Feign.builder()
				.contract(new SpringMvcContract())
				.encoder(encoder)
				.decoder(decoder)
				.requestInterceptor(requestTemplate -> {
					final CurrentUserAuthenticationDto authentication = currentUserApiService.getCurrentUserAuthentication();
					requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + authentication.jwtToken());
				})
				.target(clientType, trimTrailingSlash(url));
	}

	private String trimTrailingSlash(final String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Feign target URL cannot be empty");
		}
		return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
	}
}
