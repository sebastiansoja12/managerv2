package com.warehouse.shipment.domain.vo;


public class ConstantBodyMailMessage {

    private final String message;

    private final String labelUrl;

    private final String parcelManagementUrl;

    public ConstantBodyMailMessage(final Parcel parcel) {
        this.labelUrl = "http://localhost:8080/api/parcels/ " + "id" + "/label";
        this.parcelManagementUrl = "http://localhost:4200/shipment/client/management/" + "id";
        this.message = "Docelowa destynacja paczki to: " +
                parcel.getRecipient().getCity() + parcel.getRecipient().getStreet() +
                "\nKod państwa paczki to: " + "id" +
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
