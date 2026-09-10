package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.chat.application.port.primary.command.SendChatMessageCommand;
import com.warehouse.chat.infrastructure.adapter.primary.mapper.ChatResponseMapper;
import com.warehouse.chat.infrastructure.adapter.primary.api.SendChatMessageRequest;
import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AccessUserControl
@RequestMapping("/chat/conversations")
public class OrganizationChatController {

    private final OrganizationChatPort chatPort;
    private final ChatResponseMapper mapper;

    public OrganizationChatController(final OrganizationChatPort chatPort, final ChatResponseMapper mapper) {
        this.chatPort = chatPort;
        this.mapper = mapper;
    }

    @PostMapping("/direct/{participantUserId}")
    public ResponseEntity<?> openDirectConversation(
            @PathVariable final UserId participantUserId) {
        return ResponseEntity.ok(mapper.toResponse(chatPort.openDirectConversation(participantUserId)));
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<?> findMessages(
            @PathVariable final ChatConversationId conversationId,
            @RequestParam(required = false) final ChatMessageId beforeMessageId,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) final int limit) {
        return ResponseEntity.ok(chatPort.findMessages(conversationId, beforeMessageId, limit)
                .stream().map(mapper::toResponse).toList());
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable final ChatConversationId conversationId,
            @Valid @RequestBody final SendChatMessageRequest request) {
        final SendChatMessageCommand command = new SendChatMessageCommand(request.clientMessageId(), request.body());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(chatPort.sendMessage(conversationId, command)));
    }
}
