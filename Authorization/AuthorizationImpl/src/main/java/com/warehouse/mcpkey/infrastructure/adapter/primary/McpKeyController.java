package com.warehouse.mcpkey.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import com.warehouse.mcpkey.domain.exception.InvalidMcpKeyException;
import com.warehouse.mcpkey.domain.exception.McpKeyNotFoundException;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.GenerateMcpKeyApiRequest;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.GeneratedMcpKeyApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpAccessApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpKeyApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.McpKeyErrorApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.RevealedMcpKeyApiResponse;
import com.warehouse.mcpkey.infrastructure.adapter.primary.mapper.McpKeyApiMapper;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth/me/mcp-keys")
@AccessUserControl
public class McpKeyController {

    private final McpKeyPort mcpKeyPort;

    public McpKeyController(final McpKeyPort mcpKeyPort) {
        this.mcpKeyPort = mcpKeyPort;
    }

    @GetMapping("/access")
    public ResponseEntity<McpAccessApiResponse> access() {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(McpKeyApiMapper.toResponse(this.mcpKeyPort.findCurrentUserAccess()));
    }

    @PostMapping
    public ResponseEntity<GeneratedMcpKeyApiResponse> generate(
            @RequestBody final GenerateMcpKeyApiRequest generateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .cacheControl(CacheControl.noStore())
                .body(McpKeyApiMapper.toResponse(this.mcpKeyPort.generate(McpKeyApiMapper.toCommand(generateRequest))));
    }

    @GetMapping
    public ResponseEntity<List<McpKeyApiResponse>> list() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(this.mcpKeyPort.findCurrentUserKeys().stream()
                        .map(McpKeyApiMapper::toResponse)
                        .toList());
    }

    @PostMapping("/{mcpKeyId}/reveal")
    public ResponseEntity<RevealedMcpKeyApiResponse> reveal(@PathVariable final UUID mcpKeyId) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(McpKeyApiMapper.toResponse(this.mcpKeyPort.reveal(McpKeyApiMapper.toMcpKeyId(mcpKeyId))));
    }

    @DeleteMapping("/{mcpKeyId}")
    public ResponseEntity<Void> revoke(@PathVariable final UUID mcpKeyId) {
        this.mcpKeyPort.revoke(McpKeyApiMapper.toMcpKeyId(mcpKeyId));
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(InvalidMcpKeyException.class)
    public ResponseEntity<McpKeyErrorApiResponse> handleInvalidMcpKey(final InvalidMcpKeyException exception) {
        return ResponseEntity.badRequest()
                .body(new McpKeyErrorApiResponse("INVALID_MCP_KEY", exception.getMessage()));
    }

    @ExceptionHandler(McpKeyNotFoundException.class)
    public ResponseEntity<McpKeyErrorApiResponse> handleMcpKeyNotFound(final McpKeyNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new McpKeyErrorApiResponse("MCP_KEY_NOT_FOUND", exception.getMessage()));
    }
}
