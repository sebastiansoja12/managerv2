package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.chat.api.identificator.ChatConversationId;
import com.warehouse.chat.api.identificator.ChatMessageId;
import com.warehouse.chat.application.port.primary.OrganizationChatPort;
import com.warehouse.chat.application.port.primary.command.SendChatMessageCommand;
import com.warehouse.chat.application.port.primary.result.OpenChatConversationResult;
import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.chat.infrastructure.adapter.primary.mapper.ChatResponseMapper;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.warehouse.chat.ChatFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrganizationChatControllerTest {

    private final OrganizationChatPort chatPort = mock(OrganizationChatPort.class);
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        final DefaultFormattingConversionService conversions = new DefaultFormattingConversionService();
        conversions.addConverter(String.class, UserId.class, value -> new UserId(Long.valueOf(value)));
        conversions.addConverter(String.class, ChatConversationId.class, value -> ChatConversationId.of(Long.valueOf(value)));
        conversions.addConverter(String.class, ChatMessageId.class, value -> ChatMessageId.of(Long.valueOf(value)));
        mvc = MockMvcBuilders.standaloneSetup(new OrganizationChatController(chatPort, new ChatResponseMapper()))
                .setConversionService(conversions)
                .setControllerAdvice(new OrganizationChatExceptionHandler()).build();
    }

    @Test
    void shouldPreserveConversationResponseContract() throws Exception {
        when(chatPort.openDirectConversation(PARTICIPANT_USER_ID))
                .thenReturn(new OpenChatConversationResult(conversation(), CURRENT_USER_ID));

        mvc.perform(post("/chat/conversations/direct/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.participantUserId.value").value(2))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldMapHttpRequestToCommandAndPreserveMessageResponse() throws Exception {
        when(chatPort.sendMessage(eq(CONVERSATION_ID), any())).thenReturn(message(10L));

        mvc.perform(post("/chat/conversations/5/messages").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientMessageId\":\"" + CLIENT_MESSAGE_ID + "\",\"body\":\"Ready\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("10"))
                .andExpect(jsonPath("$.conversationId").value("5"))
                .andExpect(jsonPath("$.clientMessageId").value(CLIENT_MESSAGE_ID.toString()))
                .andExpect(jsonPath("$.body").value("Ready"))
                .andExpect(jsonPath("$.sentAt").exists());

        verify(chatPort).sendMessage(CONVERSATION_ID, new SendChatMessageCommand(CLIENT_MESSAGE_ID, "Ready"));
    }

    @Test
    void shouldRejectInvalidHttpMessageBeforeCallingPort() throws Exception {
        mvc.perform(post("/chat/conversations/5/messages").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":\" \"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(chatPort);
    }

    @ParameterizedTest
    @CsvSource({"INVALID_REQUEST,400", "ACCESS_DENIED,403", "NOT_FOUND,404", "CONFLICT,409"})
    void shouldTranslateDomainFailureToExistingProblemResponse(final ChatException.Reason reason,
                                                             final int expectedStatus) throws Exception {
        when(chatPort.openDirectConversation(PARTICIPANT_USER_ID)).thenThrow(new ChatException(reason, "Chat failure"));

        mvc.perform(post("/chat/conversations/direct/2"))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(expectedStatus))
                .andExpect(jsonPath("$.detail").value("Chat failure"));
    }
}
