package com.warehouse.onboarding.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.onboarding.domain.model.UserOnboarding;

import java.util.Optional;

public interface UserOnboardingRepository {
    Optional<UserOnboarding> findByUserId(final UserId userId);
    void save(final UserOnboarding onboarding);
}
