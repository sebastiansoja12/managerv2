package com.warehouse.mcpkey.infrastructure.adapter.secondary;

import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.entity.McpKeyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class McpKeyRepositoryImplTest {

    private final OperatorFilteredRepository<McpKeyEntity> scoped = mock(OperatorFilteredRepository.class);
    private final NoOperatorContextRepository<McpKeyEntity> authentication = mock(NoOperatorContextRepository.class);
    private final Criteria<McpKeyEntity> scopedCriteria = mock(Criteria.class, RETURNS_SELF);
    private final Criteria<McpKeyEntity> authenticationCriteria = mock(Criteria.class, RETURNS_SELF);
    private final CredentialCipher cipher = new CredentialCipher("mcp-unit-test-encryption-key");
    private final McpKeyRepositoryImpl repository = new McpKeyRepositoryImpl(scoped, authentication, cipher);
    private final McpKeyEntity entity = McpKeyFixture.entity();

    @BeforeEach
    void setUp() {
        when(scoped.createCriteria(McpKeyEntity.class)).thenReturn(scopedCriteria);
        when(authentication.createCriteria(McpKeyEntity.class)).thenReturn(authenticationCriteria);
        when(scopedCriteria.one()).thenReturn(Optional.of(entity));
        when(authenticationCriteria.one()).thenReturn(Optional.of(entity));
    }

    @Test
    void shouldStoreEncryptedSecretAndRevealItThroughScopedRepository() {
        repository.saveSecret(entity.getMcpKeyId(), McpKeyFixture.SECRET);

        assertThat(entity.getEncryptedSecret()).doesNotContain(McpKeyFixture.SECRET.value());
        assertThat(cipher.decrypt(entity.getEncryptedSecret())).isEqualTo(McpKeyFixture.SECRET.value());
        assertThat(repository.findSecretById(entity.getMcpKeyId())).contains(McpKeyFixture.SECRET);
        verify(scoped).update(entity);
    }

    @Test
    void shouldPreserveSecretWhenRecordingAuthenticationWithoutOperatorContext() {
        repository.saveSecret(entity.getMcpKeyId(), McpKeyFixture.SECRET);
        final String encrypted = entity.getEncryptedSecret();
        final McpKey key = McpKeyFixture.activeKey();
        key.recordUsage(McpKeyFixture.CREATED_AT.plusSeconds(60));

        repository.save(key);

        assertThat(entity.getEncryptedSecret()).isEqualTo(encrypted);
        assertThat(entity.getLastUsedAt()).isEqualTo(key.getLastUsedAt());
        verify(authenticationCriteria).eq("operatorId.value", McpKeyFixture.OPERATOR_ID.value());
    }

    @Test
    void shouldEraseEncryptedSecretWhenKeyIsRevoked() {
        repository.saveSecret(entity.getMcpKeyId(), McpKeyFixture.SECRET);

        repository.save(McpKeyFixture.revokedKey());

        assertThat(entity.getEncryptedSecret()).isNull();
        assertThat(repository.findSecretById(entity.getMcpKeyId())).isEmpty();
    }

    @Test
    void shouldNotRevealKeyMissingFromCurrentOperator() {
        when(scopedCriteria.one()).thenReturn(Optional.empty());

        assertThat(repository.findSecretById(entity.getMcpKeyId())).isEmpty();
    }
}
