package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserAuthenticationPort {

    UserId getCurrentUserId();

    User getCurrentUser();

    CurrentUserAuthentication getCurrentUserAuthentication();
}
