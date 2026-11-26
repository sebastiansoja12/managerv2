package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.mcpkey.McpAccessFixture;
import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.commonassets.identificator.DepartmentId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class UserRepositoryApiKeyTest {

    private final OperatorFilteredRepository<UserEntity> scoped = mock(OperatorFilteredRepository.class);
    private final NoOperatorContextRepository<UserEntity> noContext = mock(NoOperatorContextRepository.class);
    private final Criteria<UserEntity> criteria = mock(Criteria.class, RETURNS_SELF);
    private final CredentialCipher cipher = new CredentialCipher("api-key-test-encryption-key");
    private final UserRepositoryImpl repository = new UserRepositoryImpl(scoped, noContext, cipher);

    @Test
    void shouldEncryptApiKeyAndPreserveItWhenUserProfileChanges() {
        final OrganizationUser user = McpAccessFixture.user(McpKeyFixture.OWNER_ID, OrganizationUser.Role.USER,
                McpKeyFixture.OPERATOR_ID, new DepartmentId(1L));
        user.issueApiKey("auth-hash", Instant.now());

        repository.saveApiKey(user, "raw-api-secret");

        final ArgumentCaptor<UserEntity> captured = ArgumentCaptor.forClass(UserEntity.class);
        verify(scoped).update(captured.capture());
        final UserEntity stored = captured.getValue();
        assertThat(stored.getApiKey()).isEqualTo("auth-hash");
        assertThat(stored.getEncryptedApiKey()).doesNotContain("raw-api-secret");
        when(scoped.createCriteria(UserEntity.class)).thenReturn(criteria);
        when(criteria.one()).thenReturn(Optional.of(stored));
        assertThat(repository.findApiKeySecret(user.getUserId())).contains("raw-api-secret");

        user.changeLanguage("de");
        repository.createOrUpdate(user);

        final ArgumentCaptor<UserEntity> updates = ArgumentCaptor.forClass(UserEntity.class);
        org.mockito.Mockito.verify(scoped, org.mockito.Mockito.times(2)).update(updates.capture());
        assertThat(updates.getAllValues().get(1).getEncryptedApiKey()).isEqualTo(stored.getEncryptedApiKey());
    }

    @Test
    void shouldClearEncryptedKeyWhenAuthenticationKeyIsRemoved() {
        final OrganizationUser user = McpAccessFixture.user(McpKeyFixture.OWNER_ID, OrganizationUser.Role.USER,
                McpKeyFixture.OPERATOR_ID, new DepartmentId(1L));
        final UserEntity stored = UserEntity.builder().apiKey("auth-hash").encryptedApiKey(cipher.encrypt("raw-api-secret")).build();
        when(scoped.createCriteria(UserEntity.class)).thenReturn(criteria);
        when(criteria.one()).thenReturn(Optional.of(stored));

        repository.createOrUpdate(user);

        final ArgumentCaptor<UserEntity> updated = ArgumentCaptor.forClass(UserEntity.class);
        verify(scoped).update(updated.capture());
        assertThat(updated.getValue().getEncryptedApiKey()).isNull();
    }
}
