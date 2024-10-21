package com.warehouse.message.api.controller;


import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.api.MessageDto;
import com.warehouse.message.domain.model.Message;
import com.warehouse.message.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
}
