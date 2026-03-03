package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericFeignResourceService {

	private final GenericFeignClientFactory clientFactory;

	private final ObjectMapper objectMapper;

	public GenericFeignResourceService(final GenericFeignClientFactory clientFactory,
									   final ObjectMapper objectMapper) {
		this.clientFactory = clientFactory;
		this.objectMapper = objectMapper;
	}

	public <T> T findById(final String url, final Object id, final Class<T> responseType) {
		final GenericFeignResourceClient client = clientFactory.create(GenericFeignResourceClient.class, url);
		final String responseBody = client.findById(String.valueOf(id)).getBody();
		if (responseBody == null || responseBody.isBlank()) {
			return null;
		}

		try {
			return objectMapper.readValue(responseBody, responseType);
		} catch (final JsonProcessingException e) {
			throw new IllegalStateException("Cannot deserialize Feign response to " + responseType.getSimpleName(), e);
		}
	}
}
