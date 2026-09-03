package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.PlatformActorService;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.auth.infrastructure.dto.PlatformActorDto;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

public class PlatformActorServiceAdapter implements PlatformActorService {

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort;

    public PlatformActorServiceAdapter(final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort) {
        this.platformOwnerAuthenticationPort = platformOwnerAuthenticationPort;
    }

    @Override
    public Optional<PlatformActorDto> currentActor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof final PlatformOwnerId platformOwnerId) {
            return Optional.of(new PlatformActorDto(platformOwnerId.value(),
                    platformOwnerAuthenticationPort.getPlatformOwner(platformOwnerId).getUsername(), false, null, null));
        }
        return currentImpersonation()
                .map(impersonation -> new PlatformActorDto(
                        impersonation.platformOwnerId().value(),
                        impersonation.platformOwnerUsername(),
                        true,
                        authentication.getPrincipal() instanceof final UserId userId ? userId.value() : null,
                        authentication instanceof final UsernameTenantPasswordAuthenticationToken token
                                && token.getOperatorId() != null ? token.getOperatorId().value() : null));
    }

    private Optional<Impersonation> currentImpersonation() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(attributes -> attributes.getAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE,
                        RequestAttributes.SCOPE_REQUEST))
                .filter(Impersonation.class::isInstance)
                .map(Impersonation.class::cast);
    }
}
