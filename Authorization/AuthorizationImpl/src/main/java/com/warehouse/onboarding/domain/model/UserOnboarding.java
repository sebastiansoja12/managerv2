package com.warehouse.onboarding.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.onboarding.domain.enumeration.ConsentType;
import com.warehouse.onboarding.domain.enumeration.TutorialStatus;
import com.warehouse.onboarding.domain.exception.ConsentVersionMismatchException;
import com.warehouse.onboarding.domain.exception.MissingRequiredConsentException;
import com.warehouse.onboarding.domain.vo.ConsentVersion;
import com.warehouse.onboarding.domain.vo.UserConsent;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class UserOnboarding {

    private final UserId userId;
    private List<UserConsent> consents;
    private TutorialStatus tutorialStatus;
    private Instant tutorialFinishedAt;
    private final OperatorId operatorId;

    public UserOnboarding(final UserId userId,
                          final List<UserConsent> consents,
                          final TutorialStatus tutorialStatus,
                          final Instant tutorialFinishedAt,
                          final OperatorId operatorId) {
        this.userId = userId;
        this.consents = List.copyOf(consents);
        this.tutorialStatus = tutorialStatus;
        this.tutorialFinishedAt = tutorialFinishedAt;
        this.operatorId = operatorId;
    }

    public static UserOnboarding start(final UserId userId) {
        return new UserOnboarding(userId, List.of(), TutorialStatus.PENDING, null, null);
    }

    public static UserOnboarding withoutTutorial(final UserId userId) {
        return new UserOnboarding(userId, List.of(), TutorialStatus.NOT_REQUIRED, null, null);
    }

    public boolean hasAccepted(final ConsentType consentType, final ConsentVersion consentVersion) {
        return consents.stream().anyMatch(consent -> consent.covers(consentType, consentVersion));
    }

    public boolean requiresConsents(final ConsentVersion currentVersion) {
        return ConsentType.required().stream().anyMatch(consentType -> !hasAccepted(consentType, currentVersion));
    }

    public void acceptConsents(final Set<ConsentType> acceptedConsents,
                               final ConsentVersion acceptedVersion,
                               final ConsentVersion currentVersion,
                               final Instant timestamp) {
        if (!acceptedVersion.equals(currentVersion)) {
            throw new ConsentVersionMismatchException(acceptedVersion, currentVersion);
        }
        final Set<ConsentType> missingConsents = EnumSet.noneOf(ConsentType.class);
        missingConsents.addAll(ConsentType.required());
        missingConsents.removeAll(acceptedConsents);
        if (!missingConsents.isEmpty()) {
            throw new MissingRequiredConsentException(Set.copyOf(missingConsents));
        }
        this.consents = acceptedConsents.stream()
                .map(consentType -> new UserConsent(consentType, currentVersion, timestamp))
                .toList();
    }

    public void completeTutorial(final Instant timestamp) {
        finishTutorial(TutorialStatus.COMPLETED, timestamp);
    }

    public void skipTutorial(final Instant timestamp) {
        finishTutorial(TutorialStatus.SKIPPED, timestamp);
    }

    public void restartTutorial() {
        this.tutorialStatus = TutorialStatus.PENDING;
        this.tutorialFinishedAt = null;
    }

    private void finishTutorial(final TutorialStatus status, final Instant timestamp) {
        if (tutorialStatus != TutorialStatus.PENDING) {
            return;
        }
        this.tutorialStatus = status;
        this.tutorialFinishedAt = timestamp;
    }

    public UserId getUserId() {
        return userId;
    }

    public List<UserConsent> getConsents() {
        return consents;
    }

    public TutorialStatus getTutorialStatus() {
        return tutorialStatus;
    }

    public Instant getTutorialFinishedAt() {
        return tutorialFinishedAt;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }
}
