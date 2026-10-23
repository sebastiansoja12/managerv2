package com.warehouse.mcp.application.port.secondary;

import com.warehouse.mcp.domain.vo.CurrentUserProfile;

public interface CurrentUserServicePort {
    CurrentUserProfile currentUser();
}
