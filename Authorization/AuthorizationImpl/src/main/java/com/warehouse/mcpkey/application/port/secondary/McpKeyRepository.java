package com.warehouse.mcpkey.application.port.secondary;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;

import java.util.List;
import java.util.Optional;

public interface McpKeyRepository {
    void save(final McpKey mcpKey);
    void saveSecret(final McpKeyId mcpKeyId, final McpKeySecret secret);
    Optional<McpKeySecret> findSecretById(final McpKeyId mcpKeyId);
    Optional<McpKey> findById(final McpKeyId mcpKeyId);
    List<McpKey> findByOwnerId(final UserId ownerId);
    Optional<McpKey> findByKeyHash(final String keyHash);
}
