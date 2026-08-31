package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.vo.Impersonation;
import com.warehouse.auth.domain.vo.ImpersonationSession;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.PlatformOwnerAuthentication;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.Optional;

public interface PlatformOwnerAuthenticationPort {

    PlatformOwnerAuthentication login(final LoginRequest loginRequest);

    PlatformOwnerAuthentication refresh(final String refreshToken);

    void logout(final String refreshToken);

    Optional<PlatformOwner> authenticate(final String accessToken);

    PlatformOwner getPlatformOwner(final PlatformOwnerId platformOwnerId);

    boolean isActive(final PlatformOwnerId platformOwnerId);

    boolean isImpersonationActive(final Impersonation impersonation, final OperatorId operatorId);

    ImpersonationSession startImpersonation(final PlatformOwnerId platformOwnerId, final OperatorId operatorId);
}
