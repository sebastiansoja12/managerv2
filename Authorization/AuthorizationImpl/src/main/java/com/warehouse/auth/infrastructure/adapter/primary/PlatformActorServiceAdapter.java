package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.PlatformActorService;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.PlatformOwnerAuthenticationFilter;
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

    @Override
    public Optional<PlatformActorDto> currentActor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof final PlatformOwnerId platformOwnerId) {
            return currentRequestAttribute(PlatformOwnerAuthenticationFilter.PLATFORM_OWNER_USERNAME_ATTRIBUTE)
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(username -> new PlatformActorDto(platformOwnerId.value(), username, false, null, null));
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

    @Override
    public boolean isPlatformActorRequest() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && (authentication.getPrincipal() instanceof PlatformOwnerId || currentImpersonation().isPresent());
    }

    private Optional<Impersonation> currentImpersonation() {
        return currentRequestAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE)
                .filter(Impersonation.class::isInstance)
                .map(Impersonation.class::cast);
    }

    private Optional<Object> currentRequestAttribute(final String name) {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(attributes -> attributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST));
    }
}
