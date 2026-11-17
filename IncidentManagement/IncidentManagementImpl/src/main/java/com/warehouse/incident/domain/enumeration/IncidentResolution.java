package com.warehouse.incident.domain.enumeration;

public enum IncidentResolution {
    EXPLAINED(true),
    DATA_CORRECTED(false),
    DUPLICATE(false),
    INVALID_REPORT(true),
    CANCELED(true),
    REPACKED(true),
    COSMETIC_DAMAGE_ACCEPTED(false),
    DESTROYED_DISPOSED(true),
    REFUSED_BY_RECIPIENT(false),
    DAMAGE_CLAIM_FILED(true),
    FOUND(false),
    MISSORTED_REDIRECTED(false),
    DELIVERED_WITHOUT_SCAN(true),
    STOLEN(true),
    WRITTEN_OFF(true),
    ADDRESS_CORRECTED(false),
    ADDRESS_OBTAINED(false),
    REDIRECTED_TO_PICKUP_POINT(false),
    RETURN_CREATED(false),
    LABEL_REPRINTED(false),
    MANUALLY_LABELED(false),
    WEIGHT_CORRECTED(false),
    WEIGHT_CORRECTED_SURCHARGE(true),
    DOCUMENTS_RECEIVED(false),
    DOCUMENTS_REGENERATED(false),
    REROUTED(false);

    private final boolean noteRequired;

    IncidentResolution(final boolean noteRequired) {
        this.noteRequired = noteRequired;
    }

    public boolean requiresNote() {
        return noteRequired;
    }
}
