package com.warehouse.chat.application.port.secondary;

import com.warehouse.chat.domain.vo.ChatRecipient;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public interface ChatUserServicePort {

    UserId currentUserId();

    OperatorId currentOperatorId();

    Optional<ChatRecipient> findRecipient(final UserId userId);
}
