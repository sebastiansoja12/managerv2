package com.warehouse.mcpkey.infrastructure.adapter.primary;

import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import com.warehouse.mcpkey.infrastructure.adapter.primary.dto.RevealedMcpKeyApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class McpKeyControllerTest {

    @Test
    void shouldPreventCachingRevealedSecretAndRedactResponseLogging() {
        final McpKeyPort port = mock(McpKeyPort.class);
        final McpKeyController controller = new McpKeyController(port);
        final java.util.UUID keyId = McpKeyFixture.activeKey().getMcpKeyId().value();
        when(port.reveal(McpKeyFixture.activeKey().getMcpKeyId())).thenReturn(McpKeyFixture.SECRET);

        final ResponseEntity<RevealedMcpKeyApiResponse> response = controller.reveal(keyId);

        assertThat(response.getHeaders().getCacheControl()).contains("no-store");
        assertThat(response.getBody().key()).isEqualTo(McpKeyFixture.SECRET.value());
        assertThat(response.getBody().toString()).doesNotContain(McpKeyFixture.SECRET.value());
    }
}
