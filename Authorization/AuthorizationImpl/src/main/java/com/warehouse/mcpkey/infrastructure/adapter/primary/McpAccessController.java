package com.warehouse.mcpkey.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpAccessApiRequest;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpAccessApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.mapper.McpKeyApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}")
@AccessUserControl(minimumLevel = AccountLevel.ADMIN)
public class McpAccessController {

    private final McpAccessPort accessPort;

    public McpAccessController(final McpAccessPort accessPort) {
        this.accessPort = accessPort;
    }

    @GetMapping("/mcp-access")
    public ResponseEntity<McpAccessApiResponse> get(@PathVariable final UserId userId) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(McpKeyApiMapper.toResponse(this.accessPort.findManagedPolicy(userId)));
    }

    @PutMapping("/mcp-access")
    public ResponseEntity<Void> update(@PathVariable final UserId userId,
                                       @Valid @RequestBody final McpAccessApiRequest request) {
        this.accessPort.updateManagedPolicy(userId, McpKeyApiMapper.toPolicy(request));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/mcp-keys")
    public ResponseEntity<Void> revokeKeys(@PathVariable final UserId userId) {
        this.accessPort.revokeManagedKeys(userId);
        return ResponseEntity.noContent().build();
    }
}
