package com.warehouse.mcpkey.application.port.secondary;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;

public interface UserMcpKeyServicePort {
    void requireManageableUser(final UserId userId);
    void assignMcpKey(final UserId userId, final McpKeyId mcpKeyId);
    void removeMcpKey(final UserId userId, final McpKeyId mcpKeyId);
}
