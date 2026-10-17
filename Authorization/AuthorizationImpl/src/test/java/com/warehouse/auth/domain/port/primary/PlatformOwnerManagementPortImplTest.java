package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRepository;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlatformOwnerManagementPortImplTest {

    private final PlatformOwnerRepository platformOwnerRepository = mock(PlatformOwnerRepository.class);

    private final PlatformOwnerRefreshTokenRepository refreshTokenRepository = mock(PlatformOwnerRefreshTokenRepository.class);

    private final PlatformOwnerManagementPort port = new PlatformOwnerManagementPortImpl(platformOwnerRepository,
            refreshTokenRepository, mock(PasswordEncoder.class));

    @Test
    void shouldNotAllowBlockingOwnAccount() {
        final PlatformOwnerId platformOwnerId = new PlatformOwnerId(1L);

        assertThrows(IllegalStateException.class, () -> port.block(platformOwnerId, platformOwnerId));

        verify(platformOwnerRepository, never()).save(any());
    }

    @Test
    void shouldNotAllowBlockingLastActiveOwner() {
        final PlatformOwnerId platformOwnerId = new PlatformOwnerId(2L);
        when(platformOwnerRepository.findById(platformOwnerId)).thenReturn(Optional.of(owner(platformOwnerId)));
        when(platformOwnerRepository.countActive()).thenReturn(1L);

        assertThrows(IllegalStateException.class, () -> port.block(platformOwnerId, new PlatformOwnerId(1L)));

        verify(platformOwnerRepository, never()).save(any());
    }

    @Test
    void shouldBlockOwnerAndRevokeRefreshTokens() {
        final PlatformOwnerId platformOwnerId = new PlatformOwnerId(2L);
        when(platformOwnerRepository.findById(platformOwnerId)).thenReturn(Optional.of(owner(platformOwnerId)));
        when(platformOwnerRepository.countActive()).thenReturn(2L);

        port.block(platformOwnerId, new PlatformOwnerId(1L));

        verify(platformOwnerRepository).save(any(PlatformOwner.class));
        verify(refreshTokenRepository).deleteAll(platformOwnerId);
    }

    private PlatformOwner owner(final PlatformOwnerId platformOwnerId) {
        return PlatformOwner.create(platformOwnerId, "owner-" + platformOwnerId.value(), "encoded", "First", "Last",
                "owner" + platformOwnerId.value() + "@test.pl");
    }
}
