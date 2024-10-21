package com.warehouse.message.api.controller;


import static com.warehouse.commonassets.enumeration.ShipmentStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.api.MessageDto;
import com.warehouse.message.api.ShipmentStatusDto;
import com.warehouse.message.domain.model.Message;
import com.warehouse.message.service.MessageService;

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

	private ShipmentStatus determineShipmentStatus(final ShipmentStatusDto shipmentStatus) {
		return switch (shipmentStatus) {
            case CREATED -> CREATED;
            case REROUTE -> REROUTE;
            case SENT -> SENT;
            case DELIVERY -> DELIVERY;
            case RETURN -> RETURN;
            case REDIRECT -> REDIRECT;
		};
	}
}
