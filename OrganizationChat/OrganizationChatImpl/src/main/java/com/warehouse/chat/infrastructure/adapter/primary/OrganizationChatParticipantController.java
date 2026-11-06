package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.chat.infrastructure.adapter.primary.api.ChatParticipantDto;
import com.warehouse.chat.infrastructure.adapter.primary.mapper.ChatResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AccessUserControl
@RequestMapping("/chat/participants")
public class OrganizationChatParticipantController {

    private final OrganizationChatPort chatPort;
    private final ChatResponseMapper mapper;

    public OrganizationChatParticipantController(final OrganizationChatPort chatPort,
                                                 final ChatResponseMapper mapper) {
        this.chatPort = chatPort;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ChatParticipantDto>> findActiveParticipants() {
        return ResponseEntity.ok(chatPort.findActiveParticipants()
                .stream()
                .map(mapper::toResponse)
                .toList());
    }
}
