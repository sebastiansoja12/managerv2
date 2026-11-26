package com.warehouse.mcpkey.application.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;

public interface McpAccessPort {
    McpAccessPolicy findForUser(final UserId userId);
    McpAccessPolicy findManagedPolicy(final UserId userId);
    void updateManagedPolicy(final UserId userId, final McpAccessPolicy policy);
    void revokeManagedKeys(final UserId userId);
}
