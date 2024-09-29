package com.warehouse.shipment.domain.vo;


public class ConstantBodyMailMessage {

    private final String message;

    private final String labelUrl;

    private final String parcelManagementUrl;

    public ConstantBodyMailMessage(final Parcel parcel) {
        this.labelUrl = "http://localhost:8080/api/parcels/ " + parcel.getId() + "/label";
        this.parcelManagementUrl = "http://localhost:4200/shipment/client/management/" + parcel.getId();
        this.message = "Docelowa destynacja paczki to: " +
                parcel.getRecipient().city() + parcel.getRecipient().street() +
                "\nKod państwa paczki to: " + parcel.getId() +
                "\nAby pobrać etykietę prosimy wejść w " + getLabelUrl() +
                "\nAby zarządzać przesyłką prosimy wejść w: " + this.parcelManagementUrl;

    }

    public String getMessage() {
        return message;
    }

    public String getLabelUrl() {
        return labelUrl;
    }

    public String getParcelManagementUrl() {
        return parcelManagementUrl;
    }
}
