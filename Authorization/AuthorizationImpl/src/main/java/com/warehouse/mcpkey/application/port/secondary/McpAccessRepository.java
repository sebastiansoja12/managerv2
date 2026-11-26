package com.warehouse.mcpkey.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;

import java.util.Optional;

public interface McpAccessRepository {
    Optional<McpAccessPolicy> findByUserId(final UserId userId);
    void save(final UserId userId, final McpAccessPolicy policy);
}
