package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class ShipmentActorServiceAdapter implements ShipmentActorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    private final UserApiService userApiService;

    public ShipmentActorServiceAdapter(final OperatorContextProvider operatorContextProvider,
                                       final UserApiService userApiService) {
        this.operatorContextProvider = operatorContextProvider;
        this.userApiService = userApiService;
    }

    @Override
    public UserId currentUserId() {
        return operatorContextProvider.currentUserId();
    }

    @Override
    public Map<UserId, String> findUsernames(final Set<UserId> userIds) {
        final Map<UserId, String> usernames = new HashMap<>();
        userIds.forEach(userId -> findUsername(userId).ifPresent(username -> usernames.put(userId, username)));
        return usernames;
    }

    private Optional<String> findUsername(final UserId userId) {
        try {
            return Optional.ofNullable(this.userApiService.findById(userId)).map(UserDto::username);
        } catch (final RuntimeException exception) {
            return Optional.empty();
        }
    }
}
