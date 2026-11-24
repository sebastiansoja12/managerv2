package com.warehouse.mcpkey.application.port.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.application.port.primary.command.GenerateMcpKeyCommand;
import com.warehouse.mcpkey.application.port.primary.result.GeneratedMcpKey;
import com.warehouse.mcpkey.application.port.primary.result.McpKeyAuthentication;
import com.warehouse.mcpkey.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcpkey.application.port.secondary.McpKeyRepository;
import com.warehouse.mcpkey.application.port.secondary.UserMcpKeyServicePort;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;
import com.warehouse.mcpkey.domain.exception.McpKeyNotFoundException;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.service.McpKeyFactory;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class McpKeyPortImplTest {

    private static final Instant NOW = McpKeyFixture.CREATED_AT.plusSeconds(3600);

    private final InMemoryMcpKeyRepository mcpKeyRepository = new InMemoryMcpKeyRepository();
    private final CurrentUserServicePort currentUserServicePort = mock(CurrentUserServicePort.class);
    private final UserMcpKeyServicePort userMcpKeyServicePort = mock(UserMcpKeyServicePort.class);
    private final McpAccessPort accessPort = mock(McpAccessPort.class);
    private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
    private final McpKeyPortImpl port = new McpKeyPortImpl(mcpKeyRepository, new McpKeyFactory(clock),
            currentUserServicePort, userMcpKeyServicePort, clock, accessPort);

    @BeforeEach
    void setUp() {
        when(accessPort.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessPolicy.unrestricted());
        when(currentUserServicePort.currentUserId()).thenReturn(McpKeyFixture.OWNER_ID);
    }

    @Test
    void shouldRejectKeyGenerationWhenAdministratorBlockedMcp() {
        when(accessPort.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(
                new McpAccessPolicy(false, Map.of()));

        assertThatThrownBy(() -> port.generate(new GenerateMcpKeyCommand("Assistant", null)))
                .isInstanceOf(ProblemDetailsException.class);

        assertThat(mcpKeyRepository.findByOwnerId(McpKeyFixture.OWNER_ID)).isEmpty();
        verifyNoInteractions(userMcpKeyServicePort);
    }

    @Test
    void shouldGenerateKeyAndAssignItToUser() {
        final GeneratedMcpKey generatedMcpKey = port.generate(new GenerateMcpKeyCommand("Claude Code", null));

        final McpKey mcpKey = generatedMcpKey.mcpKey();
        assertThat(generatedMcpKey.secret().value()).startsWith("mcp_");
        assertThat(mcpKeyRepository.findById(mcpKey.getMcpKeyId())).isPresent();
        assertThat(mcpKey.getKeyHash()).isEqualTo(generatedMcpKey.secret().hash());
        verify(userMcpKeyServicePort).assignMcpKey(McpKeyFixture.OWNER_ID, mcpKey.getMcpKeyId());
    }

    @Test
    void shouldRevokePreviousKeyWhenGeneratingNewOne() {
        final McpKey previousKey = McpKeyFixture.activeKey();
        mcpKeyRepository.save(previousKey);

        port.generate(new GenerateMcpKeyCommand("Claude Code", null));

        assertThat(mcpKeyRepository.findById(previousKey.getMcpKeyId()).orElseThrow().getStatus())
                .isEqualTo(McpKeyStatus.REVOKED);
    }

    @Test
    void shouldRevokeOwnedKeyAndRemoveItFromUser() {
        final McpKey mcpKey = McpKeyFixture.activeKey();
        mcpKeyRepository.save(mcpKey);

        port.revoke(mcpKey.getMcpKeyId());

        assertThat(mcpKeyRepository.findById(mcpKey.getMcpKeyId()).orElseThrow().isActiveAt(NOW)).isFalse();
        verify(userMcpKeyServicePort).removeMcpKey(McpKeyFixture.OWNER_ID, mcpKey.getMcpKeyId());
    }

    @Test
    void shouldThrowExceptionWhenRevokingKeyOfAnotherUser() {
        final McpKey mcpKey = McpKeyFixture.activeKey();
        mcpKeyRepository.save(mcpKey);
        when(accessPort.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessPolicy.unrestricted());
        when(currentUserServicePort.currentUserId()).thenReturn(new UserId(6L));

        assertThatThrownBy(() -> port.revoke(mcpKey.getMcpKeyId()))
                .isInstanceOf(McpKeyNotFoundException.class);

        verifyNoInteractions(userMcpKeyServicePort);
    }

    @Test
    void shouldAuthenticateActiveKeyAndRecordUsage() {
        final McpKey mcpKey = McpKeyFixture.activeKey();
        mcpKeyRepository.save(mcpKey);

        final Optional<McpKeyAuthentication> authentication = port.authenticate(McpKeyFixture.SECRET);

        assertThat(authentication).contains(new McpKeyAuthentication(mcpKey.getMcpKeyId(), McpKeyFixture.OWNER_ID,
                McpKeyFixture.OPERATOR_ID, McpKeyFixture.CREATED_AT));
        assertThat(mcpKeyRepository.findById(mcpKey.getMcpKeyId()).orElseThrow().getLastUsedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldNotAuthenticateRevokedKey() {
        mcpKeyRepository.save(McpKeyFixture.revokedKey());

        assertThat(port.authenticate(McpKeyFixture.SECRET)).isEmpty();
    }

    @Test
    void shouldNotAuthenticateExpiredKey() {
        mcpKeyRepository.save(McpKeyFixture.keyExpiringAt(NOW.minusSeconds(1)));

        assertThat(port.authenticate(McpKeyFixture.SECRET)).isEmpty();
    }

    @Test
    void shouldNotAuthenticateUnknownKey() {
        assertThat(port.authenticate(McpKeyFixture.SECRET)).isEmpty();
    }

    @Test
    void shouldRevealOwnedActiveKeyAfterGeneration() {
        final GeneratedMcpKey generated = port.generate(new GenerateMcpKeyCommand("Assistant", null));

        assertThat(port.reveal(generated.mcpKey().getMcpKeyId())).isEqualTo(generated.secret());
    }

    @Test
    void shouldRejectRevealingAnotherUsersKey() {
        final GeneratedMcpKey generated = port.generate(new GenerateMcpKeyCommand("Assistant", null));
        final UserId otherUser = new UserId(6L);
        when(currentUserServicePort.currentUserId()).thenReturn(otherUser);
        when(accessPort.findForUser(otherUser)).thenReturn(McpAccessPolicy.unrestricted());

        assertThatThrownBy(() -> port.reveal(generated.mcpKey().getMcpKeyId()))
                .isInstanceOf(McpKeyNotFoundException.class);
    }

    @Test
    void shouldRejectRevealingBlockedExpiredRevokedOrUnknownKey() {
        final GeneratedMcpKey generated = port.generate(new GenerateMcpKeyCommand("Assistant", null));
        when(accessPort.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(new McpAccessPolicy(false, Map.of()));

        assertThatThrownBy(() -> port.reveal(generated.mcpKey().getMcpKeyId()))
                .isInstanceOf(ProblemDetailsException.class)
                .satisfies(exception -> assertThat(((ProblemDetailsException) exception).getStatus()).isEqualTo(403));

        when(accessPort.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessPolicy.unrestricted());
        port.revoke(generated.mcpKey().getMcpKeyId());
        assertThatThrownBy(() -> port.reveal(generated.mcpKey().getMcpKeyId())).isInstanceOf(McpKeyNotFoundException.class);
        mcpKeyRepository.save(McpKeyFixture.keyExpiringAt(NOW.minusSeconds(1)));
        assertThatThrownBy(() -> port.reveal(McpKeyFixture.activeKey().getMcpKeyId())).isInstanceOf(McpKeyNotFoundException.class);
        assertThatThrownBy(() -> port.reveal(McpKeyId.generate())).isInstanceOf(McpKeyNotFoundException.class);
    }

    @Test
    void shouldExplainThatLegacyHashOnlyKeyCannotBeRevealed() {
        final McpKey key = McpKeyFixture.activeKey();
        mcpKeyRepository.save(key);

        assertThatThrownBy(() -> port.reveal(key.getMcpKeyId())).isInstanceOf(ProblemDetailsException.class)
                .satisfies(exception -> assertThat(((ProblemDetailsException) exception).getStatus()).isEqualTo(409));
    }

    private static final class InMemoryMcpKeyRepository implements McpKeyRepository {

        private final Map<McpKeyId, McpKey> keys = new LinkedHashMap<>();
        private final Map<McpKeyId, McpKeySecret> secrets = new LinkedHashMap<>();

        @Override
        public void save(final McpKey mcpKey) {
            keys.put(mcpKey.getMcpKeyId(), mcpKey);
            if (mcpKey.getStatus() == McpKeyStatus.REVOKED) {
                secrets.remove(mcpKey.getMcpKeyId());
            }
        }

        @Override
        public void saveSecret(final McpKeyId keyId, final McpKeySecret secret) {
            secrets.put(keyId, secret);
        }

        @Override
        public Optional<McpKeySecret> findSecretById(final McpKeyId keyId) {
            return Optional.ofNullable(secrets.get(keyId));
        }

        @Override
        public Optional<McpKey> findById(final McpKeyId mcpKeyId) {
            return Optional.ofNullable(keys.get(mcpKeyId));
        }

        @Override
        public List<McpKey> findByOwnerId(final UserId ownerId) {
            return new ArrayList<>(keys.values().stream().filter(key -> key.isOwnedBy(ownerId)).toList());
        }

        @Override
        public Optional<McpKey> findByKeyHash(final String keyHash) {
            return keys.values().stream().filter(key -> key.getKeyHash().equals(keyHash)).findFirst();
        }
    }
}
