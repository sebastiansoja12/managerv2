package com.warehouse.pallet.domain.port.secondary;

import com.warehouse.pallet.domain.vo.UserContext;

import java.util.Optional;

public interface CurrentUserServicePort {
    Optional<UserContext> currentUser();
}
