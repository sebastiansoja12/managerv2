package com.warehouse.message.domain.vo;

import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.api.MessageContentUpdateRequestDto;

public record MessageContentUpdateRequest(MessageId messageId, String messageContent) {
	public static MessageContentUpdateRequest from(final MessageContentUpdateRequestDto messageContentUpdateRequest) {
		return new MessageContentUpdateRequest(new MessageId(messageContentUpdateRequest.messageId().value()),
				messageContentUpdateRequest.messageContent().getValue());
	}
}
