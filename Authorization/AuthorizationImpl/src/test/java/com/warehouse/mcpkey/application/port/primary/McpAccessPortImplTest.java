package com.warehouse.mcpkey.application.port.primary;

import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.application.port.secondary.McpAccessRepository;
import com.warehouse.mcpkey.application.port.secondary.McpKeyRepository;
import com.warehouse.mcpkey.application.port.secondary.UserMcpKeyServicePort;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.model.McpKey;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class McpAccessPortImplTest {

    private final McpAccessRepository accessRepository = mock(McpAccessRepository.class);
    private final McpKeyRepository keyRepository = mock(McpKeyRepository.class);
    private final UserMcpKeyServicePort userService = mock(UserMcpKeyServicePort.class);
    private final McpAccessPort port = new McpAccessPortImpl(accessRepository, keyRepository, userService,
            Clock.fixed(McpKeyFixture.CREATED_AT, ZoneOffset.UTC));

    @Test
    void shouldPreserveExistingAccessUntilAdministratorSetsPolicy() {
        assertThat(port.findForUser(McpKeyFixture.OWNER_ID)).isEqualTo(McpAccessPolicy.unrestricted());
    }

    @Test
    void shouldRevokeKeysWhenAccessIsDisabled() {
        final McpAccessPolicy blocked = new McpAccessPolicy(false, Map.of());
        final McpKey key = McpKeyFixture.activeKey();
        when(keyRepository.findByOwnerId(McpKeyFixture.OWNER_ID)).thenReturn(List.of(key));

        port.updateManagedPolicy(McpKeyFixture.OWNER_ID, blocked);

        verify(accessRepository).save(McpKeyFixture.OWNER_ID, blocked);
        verify(keyRepository).save(key);
        verify(userService).removeMcpKey(McpKeyFixture.OWNER_ID, key.getMcpKeyId());
        assertThat(key.isActiveAt(McpKeyFixture.CREATED_AT)).isFalse();
    }

    @Test
    void shouldNotRestoreKeysWhenAccessIsReenabled() {
        port.updateManagedPolicy(McpKeyFixture.OWNER_ID, McpAccessPolicy.unrestricted());

        verify(accessRepository).save(McpKeyFixture.OWNER_ID, McpAccessPolicy.unrestricted());
        verifyNoInteractions(keyRepository);
    }

    @Test
    void shouldRejectReadingOrChangingPolicyAndRevokingKeysOutsideAdminScope() {
        doThrow(new ProblemDetailsException(403, "Forbidden")).when(userService).requireManageableUser(McpKeyFixture.OWNER_ID);

        assertThatThrownBy(() -> port.findManagedPolicy(McpKeyFixture.OWNER_ID)).isInstanceOf(ProblemDetailsException.class);
        assertThatThrownBy(() -> port.updateManagedPolicy(McpKeyFixture.OWNER_ID, McpAccessPolicy.unrestricted()))
                .isInstanceOf(ProblemDetailsException.class);
        assertThatThrownBy(() -> port.revokeManagedKeys(McpKeyFixture.OWNER_ID)).isInstanceOf(ProblemDetailsException.class);

        verifyNoInteractions(accessRepository, keyRepository);
    }
}
