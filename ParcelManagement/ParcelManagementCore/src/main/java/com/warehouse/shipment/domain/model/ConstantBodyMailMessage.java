package com.warehouse.shipment.domain.model;

import lombok.Data;

@Data
public class ConstantBodyMailMessage {

    private String message;

    private Long parcelId;

    private String url;

    private String labelUrl;

    private String parcelManagementUrl;

    private String city;

    private String street;

    public ConstantBodyMailMessage(Parcel parcel) {
        this.labelUrl = "http://localhost:8080/api/parcels/ " + parcel.getId() + "/label";
        this.parcelManagementUrl = "http://localhost:4200/shipment/client/management/" + parcel.getId();
        this.message = "Docelowa destynacja paczki to: " +
                parcel.getRecipient().getCity() + parcel.getRecipient().getStreet() +
                "\nKod państwa paczki to: " + parcel.getId() +
                "\nAby pobrać etykietę prosimy wejść w " + getLabelUrl() +
                "\nAby zarządzać przesyłką prosimy wejść w: " + this.parcelManagementUrl;

    }

}
