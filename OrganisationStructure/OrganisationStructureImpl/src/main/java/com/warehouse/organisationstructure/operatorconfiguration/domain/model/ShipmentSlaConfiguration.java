package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentSlaConfiguration {
    private int economySlaHours;
    private int standardSlaHours;
    private int expressSlaHours;
    private int flagDelayedAfterHours;
    private boolean requireExceptionNote;
    private boolean escalateDelaysToDepartmentManager;

    public ShipmentSlaConfiguration() {
    }

    public ShipmentSlaConfiguration(final int economySlaHours,
                                    final int standardSlaHours,
                                    final int expressSlaHours,
                                    final int flagDelayedAfterHours,
                                    final boolean requireExceptionNote,
                                    final boolean escalateDelaysToDepartmentManager) {
        this.economySlaHours = economySlaHours;
        this.standardSlaHours = standardSlaHours;
        this.expressSlaHours = expressSlaHours;
        this.flagDelayedAfterHours = flagDelayedAfterHours;
        this.requireExceptionNote = requireExceptionNote;
        this.escalateDelaysToDepartmentManager = escalateDelaysToDepartmentManager;
    }

    public static ShipmentSlaConfiguration defaultConfiguration() {
        return new ShipmentSlaConfiguration(96, 48, 24, 24, true, false);
    }

    public int getEconomySlaHours() { return economySlaHours; }
    public int getStandardSlaHours() { return standardSlaHours; }
    public int getExpressSlaHours() { return expressSlaHours; }
    public int getFlagDelayedAfterHours() { return flagDelayedAfterHours; }
    public boolean isRequireExceptionNote() { return requireExceptionNote; }
    public boolean isEscalateDelaysToDepartmentManager() { return escalateDelaysToDepartmentManager; }
}
