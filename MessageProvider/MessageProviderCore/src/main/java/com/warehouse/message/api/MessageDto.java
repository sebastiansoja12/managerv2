package com.warehouse.message.api;

import com.warehouse.message.domain.model.Message;

public class MessageDto {
    
    private MessageIdDto messageId;
    
    private TitleDto title;

    private ShipmentStatusDto shipmentStatus;

    private LanguageDto language;

    private MessageContentDto messageContent;

    private SenderDto sender;

    public MessageDto() {
    }

	public MessageDto(final MessageIdDto messageId, 
                      final TitleDto title, 
                      final ShipmentStatusDto shipmentStatus, 
                      final LanguageDto language,
                      final MessageContentDto messageContent, 
                      final SenderDto sender) {
        this.messageId = messageId;
        this.title = title;
        this.shipmentStatus = shipmentStatus;
        this.language = language;
        this.messageContent = messageContent;
        this.sender = sender;
    }

    public MessageIdDto getMessageId() {
        return messageId;
    }

    public TitleDto getTitle() {
        return title;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public LanguageDto getLanguage() {
        return language;
    }

    public MessageContentDto getMessageContent() {
        return messageContent;
    }

    public SenderDto getSender() {
        return sender;
    }
    
    public static MessageDto from(final Message message) {
        final MessageIdDto messageId = MessageIdDto.from(message);
        final TitleDto title = TitleDto.from(message);
        final ShipmentStatusDto shipmentStatus = ShipmentStatusDto.from(message);
        final LanguageDto language = LanguageDto.from(message);
        final SenderDto sender = SenderDto.from(message);
        final MessageContentDto messageContent = MessageContentDto.from(message);
		return new MessageDto(messageId, title, shipmentStatus, language,
				messageContent, sender);
    }
}
