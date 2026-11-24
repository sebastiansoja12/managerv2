package com.warehouse.mcpkey.application.port.primary;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.mcpkey.application.port.primary.command.GenerateMcpKeyCommand;
import com.warehouse.mcpkey.application.port.primary.result.GeneratedMcpKey;
import com.warehouse.mcpkey.application.port.primary.result.McpKeyAuthentication;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;

import java.util.List;
import java.util.Optional;

public interface McpKeyPort {
    GeneratedMcpKey generate(final GenerateMcpKeyCommand command);
    List<McpKey> findCurrentUserKeys();
    McpAccessPolicy findCurrentUserAccess();
    McpKeySecret reveal(final McpKeyId mcpKeyId);
    void revoke(final McpKeyId mcpKeyId);
    Optional<McpKeyAuthentication> authenticate(final McpKeySecret secret);
}
