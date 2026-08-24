package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentValidationConfiguration {
    private boolean validateAddressData;
    private boolean requireRecipientPhone;
    private boolean requireRecipientEmail;
    private boolean preventDuplicateTracking;
    private boolean requireSenderReference;
    private boolean validatePostalCode;

    public ShipmentValidationConfiguration() {
    }

    public ShipmentValidationConfiguration(final boolean validateAddressData,
                                           final boolean requireRecipientPhone,
                                           final boolean requireRecipientEmail,
                                           final boolean preventDuplicateTracking,
                                           final boolean requireSenderReference,
                                           final boolean validatePostalCode) {
        this.validateAddressData = validateAddressData;
        this.requireRecipientPhone = requireRecipientPhone;
        this.requireRecipientEmail = requireRecipientEmail;
        this.preventDuplicateTracking = preventDuplicateTracking;
        this.requireSenderReference = requireSenderReference;
        this.validatePostalCode = validatePostalCode;
    }

    public static ShipmentValidationConfiguration defaultConfiguration() {
        return new ShipmentValidationConfiguration(true, true, false, true, false, true);
    }

    public boolean isValidateAddressData() { return validateAddressData; }
    public boolean isRequireRecipientPhone() { return requireRecipientPhone; }
    public boolean isRequireRecipientEmail() { return requireRecipientEmail; }
    public boolean isPreventDuplicateTracking() { return preventDuplicateTracking; }
    public boolean isRequireSenderReference() { return requireSenderReference; }
    public boolean isValidatePostalCode() { return validatePostalCode; }
}
