package com.warehouse.message.controller;


import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.AccessAudience;
import static com.warehouse.commonassets.enumeration.ShipmentStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.api.*;
import com.warehouse.message.domain.model.Message;
import com.warehouse.message.domain.service.MessageService;
import com.warehouse.message.domain.vo.MessageContentUpdateRequest;
import com.warehouse.message.domain.vo.SenderUpdateRequest;
import com.warehouse.message.domain.vo.TitleUpdateRequest;

@AccessUserControl
@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/sender/{name}")
    public ResponseEntity<?> messagesBySender(@PathVariable("name") final String name) {
        final List<Message> messages = messageService.findBySender(name);
		return ResponseEntity.ok(messages
                .stream()
                .map(MessageDto::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> messageByTitle(@PathVariable("title") final String title) {
        final Message message = messageService.findByTitle(title);
        return ResponseEntity.ok(MessageDto.from(message));
    }

	@GetMapping("/{id}")
    public ResponseEntity<?> messageById(@PathVariable final Long id) {
        final MessageId messageId = new MessageId(id);
        final Message message = messageService.findByMessageId(messageId);
        return ResponseEntity.ok(MessageDto.from(message));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<?> messageById(@PathVariable final String language) {
        final List<Message> messages = messageService.findByLanguage(language);
        return ResponseEntity.ok(messages
                .stream()
                .map(MessageDto::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/shipmentstatus/{shipmentStatus}")
    public ResponseEntity<?> messageByShipmentStatus(@PathVariable final ShipmentStatusDto shipmentStatus) {
        final ShipmentStatus status = determineShipmentStatus(shipmentStatus);
        final List<Message> messages = messageService.findByShipmentStatus(status);
        return ResponseEntity.ok(messages
                .stream()
                .map(MessageDto::from)
                .collect(Collectors.toList()));
    }

    @PutMapping("/title")
    @AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
    public ResponseEntity<?> updateMessageTitle(@RequestBody final TitleUpdateRequestDto titleUpdateRequest) {
        final TitleUpdateRequest request = TitleUpdateRequest.from(titleUpdateRequest);
        messageService.updateMessageTitle(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/sender")
    @AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
    public ResponseEntity<?> updateMessageSender(@RequestBody final SenderUpdateRequestDto senderUpdateRequest) {
        final SenderUpdateRequest request = SenderUpdateRequest.from(senderUpdateRequest);
        messageService.updateMessageSender(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/content")
    @AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
    public ResponseEntity<?> updateMessageContent(@RequestBody final MessageContentUpdateRequestDto messageContentUpdateRequest) {
        final MessageContentUpdateRequest request = MessageContentUpdateRequest.from(messageContentUpdateRequest);
        messageService.updateMessageContent(request);
        return ResponseEntity.ok().build();
    }

	private ShipmentStatus determineShipmentStatus(final ShipmentStatusDto shipmentStatus) {
		return switch (shipmentStatus) {
            case PLANNED -> PLANNED;
            case CREATED -> CREATED;
            case PREPARED -> PREPARED;
            case COLLECTED -> COLLECTED;
            case ACCEPTED -> ACCEPTED;
            case REROUTE -> REROUTE;
            case SENT -> SENT;
            case HANDOVER -> HANDOVER;
            case DELIVERY -> DELIVERY;
            case RETURN -> RETURN;
            case REDIRECT -> REDIRECT;
		};
	}
}
