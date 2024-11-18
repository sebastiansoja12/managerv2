package com.warehouse.message.api;

import com.warehouse.message.domain.model.Message;

public record SenderDto(String value) {

	public static SenderDto from(final Message message) {
		return new SenderDto(message.getSender());
	}
}
