package com.warehouse.onboarding.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.onboarding.domain.enumeration.TutorialStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "onboarding.UserOnboardingEntity")
@Table(name = "user_onboarding")
public class UserOnboardingEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_onboarding_consents", joinColumns = @JoinColumn(name = "user_id"))
    private List<UserConsentEmbeddable> consents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "tutorial_status", nullable = false, length = 30)
    private TutorialStatus tutorialStatus;

    @Column(name = "tutorial_finished_at")
    private Instant tutorialFinishedAt;

    protected UserOnboardingEntity() {
    }

    public UserOnboardingEntity(final UserId userId,
                                final List<UserConsentEmbeddable> consents,
                                final TutorialStatus tutorialStatus,
                                final Instant tutorialFinishedAt) {
        this.userId = userId;
        this.consents = new ArrayList<>(consents);
        this.tutorialStatus = tutorialStatus;
        this.tutorialFinishedAt = tutorialFinishedAt;
    }

    public UserId getUserId() {
        return userId;
    }

    public List<UserConsentEmbeddable> getConsents() {
        return consents;
    }

    public TutorialStatus getTutorialStatus() {
        return tutorialStatus;
    }

    public Instant getTutorialFinishedAt() {
        return tutorialFinishedAt;
    }
}
