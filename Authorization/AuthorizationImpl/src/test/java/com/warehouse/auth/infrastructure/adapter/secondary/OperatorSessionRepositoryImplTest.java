package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache.CachedSessionRevocation;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.OperatorSessionRevocationEntity;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorSessionRepositoryImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(7L);
    private static final Instant REVOKED_AT = Instant.parse("2026-11-02T08:00:00Z");

    @Mock
    private RefreshTokenReadRepository refreshTokenReadRepository;

    @Mock
    private OperatorSessionRevocationReadRepository revocationReadRepository;

    @Mock
    private OperatorAccessCache operatorAccessCache;

    private OperatorSessionRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new OperatorSessionRepositoryImpl(refreshTokenReadRepository, revocationReadRepository,
                operatorAccessCache);
    }

    @Test
    void shouldReturnCachedRevocationWithoutQueryingDatabase() {
        when(operatorAccessCache.findSessionRevocation(OPERATOR_ID))
                .thenReturn(Optional.of(new CachedSessionRevocation(REVOKED_AT)));

        assertThat(repository.findSessionsRevokedAt(OPERATOR_ID)).contains(REVOKED_AT);
        verifyNoInteractions(revocationReadRepository);
    }

    @Test
    void shouldLoadRevocationFromDatabaseAndCacheItOnMiss() {
        when(operatorAccessCache.findSessionRevocation(OPERATOR_ID)).thenReturn(Optional.empty());
        when(revocationReadRepository.findById(7L)).thenReturn(Optional.empty());

        assertThat(repository.findSessionsRevokedAt(OPERATOR_ID)).isEmpty();
        verify(operatorAccessCache).putSessionRevocation(OPERATOR_ID, new CachedSessionRevocation(null));
    }

    @Test
    void shouldWriteRevocationThroughToCache() {
        repository.markSessionsRevoked(OPERATOR_ID, REVOKED_AT);

        verify(revocationReadRepository).save(any(OperatorSessionRevocationEntity.class));
        verify(operatorAccessCache).putSessionRevocation(OPERATOR_ID, new CachedSessionRevocation(REVOKED_AT));
    }
}
