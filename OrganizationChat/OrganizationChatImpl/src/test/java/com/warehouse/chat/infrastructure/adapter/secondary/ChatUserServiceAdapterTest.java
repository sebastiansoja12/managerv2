package com.warehouse.chat.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.OperatorIdDto;
import com.warehouse.chat.domain.vo.ChatRecipient;
import org.junit.jupiter.api.Test;

import static com.warehouse.chat.ChatFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChatUserServiceAdapterTest {

    private final CurrentUserApiService currentUser = mock(CurrentUserApiService.class);
    private final CurrentOperatorService currentOperator = mock(CurrentOperatorService.class);
    private final UserApiService users = mock(UserApiService.class);
    private final ChatUserServiceAdapter adapter = new ChatUserServiceAdapter(currentUser, currentOperator, users);

    @Test
    void shouldUseExistingAuthenticatedUserAndOrganization() {
        when(currentUser.getCurrentUserId()).thenReturn(CURRENT_USER_ID);
        when(currentOperator.getCurrentOperatorId()).thenReturn(OPERATOR_ID);

        assertThat(adapter.currentUserId()).isEqualTo(CURRENT_USER_ID);
        assertThat(adapter.currentOperatorId()).isEqualTo(OPERATOR_ID);
    }

    @Test
    void shouldTranslateExternalRecipientToDomainValue() {
        when(users.findById(PARTICIPANT_USER_ID))
                .thenReturn(ChatUserFixture.recipient(false, new OperatorIdDto(OPERATOR_ID.value())));

        assertThat(adapter.findRecipient(PARTICIPANT_USER_ID)).contains(new ChatRecipient(OPERATOR_ID, true));
    }

    @Test
    void shouldRepresentMissingRecipientAsEmpty() {
        assertThat(adapter.findRecipient(PARTICIPANT_USER_ID)).isEmpty();
    }

    @Test
    void shouldPreserveInactiveStatusAndAbsentOrganization() {
        when(users.findById(PARTICIPANT_USER_ID)).thenReturn(ChatUserFixture.recipient(true, null));

        assertThat(adapter.findRecipient(PARTICIPANT_USER_ID)).contains(new ChatRecipient(null, false));
    }
}
