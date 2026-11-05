package com.warehouse.onboarding.application.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.onboarding.application.port.primary.command.AcceptConsentsCommand;
import com.warehouse.onboarding.application.port.primary.result.UserOnboardingResult;

public interface UserOnboardingPort {
    void start(final UserId userId);
    UserOnboardingResult getCurrentUserOnboarding();
    UserOnboardingResult acceptConsents(final AcceptConsentsCommand command);
    UserOnboardingResult completeTutorial();
    UserOnboardingResult skipTutorial();
    UserOnboardingResult restartTutorial();
}
