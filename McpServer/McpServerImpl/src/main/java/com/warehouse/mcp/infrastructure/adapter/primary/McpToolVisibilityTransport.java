package com.warehouse.mcp.infrastructure.adapter.primary;

import com.warehouse.auth.Mcp;
import com.warehouse.auth.McpToolAccessApiService;
import io.modelcontextprotocol.common.McpTransportContext;
import io.modelcontextprotocol.server.McpStatelessServerHandler;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpStatelessServerTransport;
import org.springframework.ai.tool.annotation.Tool;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class McpToolVisibilityTransport implements McpStatelessServerTransport {

    private final McpStatelessServerTransport delegate;
    private final McpToolAccessApiService accessService;
    private final boolean writeEnabled;
    private final Map<String, Mcp> rules = new HashMap<>();

    public McpToolVisibilityTransport(final McpStatelessServerTransport delegate,
                                       final McpToolAccessApiService accessService, final boolean writeEnabled) {
        this.delegate = delegate;
        this.accessService = accessService;
        this.writeEnabled = writeEnabled;
        for (final Class<?> toolClass : List.of(ManagerMcpTools.class, ManagerMcpCommandTools.class)) {
            for (final Method method : toolClass.getMethods()) {
                final Tool tool = method.getAnnotation(Tool.class);
                if (tool != null) {
                    final Mcp rule = method.getAnnotation(Mcp.class);
                    if (rule == null) {
                        throw new IllegalStateException("MCP tool requires access metadata: " + tool.name());
                    }
                    this.rules.put(tool.name(), rule);
                }
            }
        }
    }

    @Override
    public void setMcpHandler(final McpStatelessServerHandler handler) {
        this.delegate.setMcpHandler(new McpStatelessServerHandler() {
            @Override
            public Mono<McpSchema.JSONRPCResponse> handleRequest(final McpTransportContext context,
                                                                  final McpSchema.JSONRPCRequest request) {
                if (!McpSchema.METHOD_TOOLS_LIST.equals(request.method())) {
                    return handler.handleRequest(context, request);
                }
                final Set<String> permitted = accessService.allowedTools(Map.copyOf(rules));
                final Set<String> allowed = rules.entrySet().stream()
                        .filter(entry -> permitted.contains(entry.getKey()))
                        .filter(entry -> !entry.getValue().write() || writeEnabled)
                        .map(Map.Entry::getKey).collect(Collectors.toSet());
                return handler.handleRequest(context, request).map(response -> {
                    if (!(response.result() instanceof final McpSchema.ListToolsResult result)) {
                        return response;
                    }
                    return new McpSchema.JSONRPCResponse(response.jsonrpc(), response.id(),
                            new McpSchema.ListToolsResult(result.tools().stream()
                                    .filter(tool -> allowed.contains(tool.name())).toList(), result.nextCursor(), result.meta()),
                            response.error());
                });
            }

            @Override
            public Mono<Void> handleNotification(final McpTransportContext context,
                                                  final McpSchema.JSONRPCNotification notification) {
                return handler.handleNotification(context, notification);
            }
        });
    }

    @Override
    public Mono<Void> closeGracefully() {
        return this.delegate.closeGracefully();
    }

    @Override
    public List<String> protocolVersions() {
        return this.delegate.protocolVersions();
    }
}
