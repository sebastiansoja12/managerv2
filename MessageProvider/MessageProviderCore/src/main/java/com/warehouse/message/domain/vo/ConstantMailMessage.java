package com.warehouse.message.domain.vo;

import com.warehouse.message.domain.model.Message;

public record ConstantMailMessage(String messageContent, String title, String sender) {

	public ConstantMailMessage from(final Message message) {
		return new ConstantMailMessage(message.getMessageContent(), message.getTitle(), message.getSender());
	}
}
