package com.warehouse.onboarding.domain.model;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.onboarding.domain.enumeration.ConsentType;
import com.warehouse.onboarding.domain.enumeration.TutorialStatus;
import com.warehouse.onboarding.domain.exception.ConsentVersionMismatchException;
import com.warehouse.onboarding.domain.exception.MissingRequiredConsentException;
import com.warehouse.onboarding.domain.vo.ConsentVersion;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserOnboardingTest {

    private static final UserId USER_ID = new UserId(7L);
    private static final ConsentVersion VERSION = new ConsentVersion("2026-11");
    private static final Instant NOW = Instant.parse("2026-11-05T10:00:00Z");

    @Test
    void shouldRequireConsentsWhenNothingWasAccepted() {
        final UserOnboarding onboarding = UserOnboarding.start(USER_ID);

        assertThat(onboarding.requiresConsents(VERSION)).isTrue();
        assertThat(onboarding.getTutorialStatus()).isEqualTo(TutorialStatus.PENDING);
    }

    @Test
    void shouldAcceptAllRequiredConsents() {
        final UserOnboarding onboarding = UserOnboarding.start(USER_ID);

        onboarding.acceptConsents(ConsentType.required(), VERSION, VERSION, NOW);

        assertThat(onboarding.requiresConsents(VERSION)).isFalse();
        assertThat(onboarding.hasAccepted(ConsentType.PRODUCT_UPDATES, VERSION)).isFalse();
    }

    @Test
    void shouldRequireConsentsAgainWhenVersionChanged() {
        final UserOnboarding onboarding = UserOnboarding.start(USER_ID);
        onboarding.acceptConsents(ConsentType.required(), VERSION, VERSION, NOW);

        assertThat(onboarding.requiresConsents(new ConsentVersion("2027-01"))).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenRequiredConsentIsMissing() {
        final UserOnboarding onboarding = UserOnboarding.start(USER_ID);

        final MissingRequiredConsentException exception = assertThrows(MissingRequiredConsentException.class,
                () -> onboarding.acceptConsents(Set.of(ConsentType.TERMS_OF_SERVICE), VERSION, VERSION, NOW));

        assertThat(exception.getMissingConsents())
                .containsExactlyInAnyOrder(ConsentType.PRIVACY_POLICY, ConsentType.PERSONAL_DATA_PROCESSING);
    }

    @Test
    void shouldThrowExceptionWhenAcceptedVersionIsOutdated() {
        final UserOnboarding onboarding = UserOnboarding.start(USER_ID);

        assertThrows(ConsentVersionMismatchException.class,
                () -> onboarding.acceptConsents(ConsentType.required(), new ConsentVersion("2026-01"), VERSION, NOW));
    }

    @Test
    void shouldKeepFirstTutorialDecision() {
        final UserOnboarding onboarding = UserOnboarding.start(USER_ID);

        onboarding.skipTutorial(NOW);
        onboarding.completeTutorial(NOW.plusSeconds(60));

        assertThat(onboarding.getTutorialStatus()).isEqualTo(TutorialStatus.SKIPPED);
        assertThat(onboarding.getTutorialFinishedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldRestartTutorial() {
        final UserOnboarding onboarding = UserOnboarding.withoutTutorial(USER_ID);

        onboarding.restartTutorial();

        assertThat(onboarding.getTutorialStatus()).isEqualTo(TutorialStatus.PENDING);
    }
}
