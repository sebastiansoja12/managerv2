package com.warehouse.mcp.infrastructure.adapter.primary;

import com.warehouse.auth.McpToolAccessApiService;
import io.modelcontextprotocol.common.McpTransportContext;
import io.modelcontextprotocol.server.McpStatelessServerHandler;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpStatelessServerTransport;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class McpToolVisibilityTransportTest {

    private final McpStatelessServerTransport delegate = mock(McpStatelessServerTransport.class);
    private final McpToolAccessApiService access = mock(McpToolAccessApiService.class);
    private final McpStatelessServerHandler handler = mock(McpStatelessServerHandler.class);

    @Test
    void shouldHideDisallowedToolsAndReevaluateEachRequest() {
        final McpStatelessServerHandler filtered = register(true);
        when(access.allowedTools(anyMap())).thenReturn(Set.of("find_shipment"));

        assertThat(list(filtered)).extracting(McpSchema.Tool::name).containsExactly("find_shipment");

        when(access.allowedTools(anyMap())).thenReturn(Set.of("get_configuration"));

        assertThat(list(filtered)).extracting(McpSchema.Tool::name).containsExactly("get_configuration");
    }

    @Test
    void shouldHideWriteToolsWhenGlobalWritesAreDisabled() {
        final McpStatelessServerHandler filtered = register(false);
        when(access.allowedTools(anyMap())).thenReturn(Set.of("find_shipment", "create_shipment", "get_configuration"));

        assertThat(list(filtered)).extracting(McpSchema.Tool::name)
                .containsExactly("find_shipment", "get_configuration");
    }

    private McpStatelessServerHandler register(final boolean writeEnabled) {
        final McpToolVisibilityTransport transport = new McpToolVisibilityTransport(delegate, access, writeEnabled);
        final List<McpSchema.Tool> tools = List.of(tool("find_shipment"), tool("create_shipment"), tool("get_configuration"),
                tool("unknown_unprotected_tool"));
        when(handler.handleRequest(any(), any())).thenReturn(Mono.just(
                new McpSchema.JSONRPCResponse("2.0", 1, new McpSchema.ListToolsResult(tools, null), null)));
        transport.setMcpHandler(handler);
        final ArgumentCaptor<McpStatelessServerHandler> capture = ArgumentCaptor.forClass(McpStatelessServerHandler.class);
        verify(delegate).setMcpHandler(capture.capture());
        return capture.getValue();
    }

    private List<McpSchema.Tool> list(final McpStatelessServerHandler filtered) {
        final McpSchema.JSONRPCResponse response = filtered.handleRequest(McpTransportContext.EMPTY,
                new McpSchema.JSONRPCRequest("2.0", McpSchema.METHOD_TOOLS_LIST, 1, null)).block();
        return ((McpSchema.ListToolsResult) response.result()).tools();
    }

    private McpSchema.Tool tool(final String name) {
        return McpSchema.Tool.builder().name(name).description(name).inputSchema(new McpSchema.JsonSchema("object", Map.of(), List.of(), false, null, null)).build();
    }
}
