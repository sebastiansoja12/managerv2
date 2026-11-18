package com.warehouse.onboarding.application.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.onboarding.application.port.primary.command.AcceptConsentsCommand;
import com.warehouse.onboarding.application.port.primary.result.ConsentRequirementResult;
import com.warehouse.onboarding.application.port.primary.result.UserOnboardingResult;
import com.warehouse.onboarding.application.port.secondary.CurrentUserServicePort;
import com.warehouse.onboarding.application.port.secondary.UserOnboardingRepository;
import com.warehouse.onboarding.domain.enumeration.ConsentType;
import com.warehouse.onboarding.domain.model.UserOnboarding;
import com.warehouse.onboarding.domain.vo.ConsentVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Arrays;
import java.util.function.Consumer;

@Slf4j
public class UserOnboardingPortImpl implements UserOnboardingPort {

    private final UserOnboardingRepository userOnboardingRepository;
    private final CurrentUserServicePort currentUserServicePort;
    private final Clock clock;
    private final ConsentVersion currentConsentVersion;

    public UserOnboardingPortImpl(final UserOnboardingRepository userOnboardingRepository,
                                  final CurrentUserServicePort currentUserServicePort,
                                  final Clock clock,
                                  final ConsentVersion currentConsentVersion) {
        this.userOnboardingRepository = userOnboardingRepository;
        this.currentUserServicePort = currentUserServicePort;
        this.clock = clock;
        this.currentConsentVersion = currentConsentVersion;
    }

    @Override
    @Transactional
    public void start(final UserId userId) {
        this.userOnboardingRepository.save(UserOnboarding.start(userId));
        log.info("Onboarding started for user {}", userId.value());
    }

    @Override
    public UserOnboardingResult getCurrentUserOnboarding() {
        return toResult(currentUserOnboarding());
    }

    @Override
    @Transactional
    public UserOnboardingResult acceptConsents(final AcceptConsentsCommand command) {
        return change(onboarding -> onboarding.acceptConsents(command.acceptedConsents(), command.version(),
                this.currentConsentVersion, this.clock.instant()));
    }

    @Override
    @Transactional
    public UserOnboardingResult completeTutorial() {
        return change(onboarding -> onboarding.completeTutorial(this.clock.instant()));
    }

    @Override
    @Transactional
    public UserOnboardingResult skipTutorial() {
        return change(onboarding -> onboarding.skipTutorial(this.clock.instant()));
    }

    @Override
    @Transactional
    public UserOnboardingResult restartTutorial() {
        return change(UserOnboarding::restartTutorial);
    }

    private UserOnboardingResult change(final Consumer<UserOnboarding> change) {
        final UserOnboarding onboarding = currentUserOnboarding();
        change.accept(onboarding);
        this.userOnboardingRepository.save(onboarding);
        return toResult(onboarding);
    }

    private UserOnboarding currentUserOnboarding() {
        final UserId userId = this.currentUserServicePort.currentUserId();
        return this.userOnboardingRepository.findByUserId(userId)
                .orElseGet(() -> UserOnboarding.withoutTutorial(userId));
    }

    private UserOnboardingResult toResult(final UserOnboarding onboarding) {
        return new UserOnboardingResult(
                onboarding.requiresConsents(this.currentConsentVersion),
                this.currentConsentVersion,
                Arrays.stream(ConsentType.values())
                        .map(consentType -> new ConsentRequirementResult(consentType, consentType.isRequired(),
                                onboarding.hasAccepted(consentType, this.currentConsentVersion)))
                        .toList(),
                onboarding.getTutorialStatus());
    }
}
