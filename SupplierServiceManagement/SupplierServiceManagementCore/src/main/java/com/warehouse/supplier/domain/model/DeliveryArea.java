package com.warehouse.supplier.domain.model;

import java.util.Set;

public class DeliveryArea {

    private String areaName;
    private String city;
    private String district;
    private String municipality;
    private String region;
    private String country;
    private Set<String> postalCodes;

    public DeliveryArea(final String areaName,
                        final String city,
                        final String district,
                        final String municipality,
                        final String region,
                        final String country,
                        final Set<String> postalCodes) {
        this.areaName = areaName;
        this.city = city;
        this.district = district;
        this.municipality = municipality;
        this.region = region;
        this.country = country;
        this.postalCodes = postalCodes;
    }

    public final boolean coversPostalCode(final String postalCode) {
        return postalCodes != null && postalCodes.contains(postalCode);
    }

    public final String getAreaName() {
        return areaName;
    }

    public final void setAreaName(final String areaName) {
        this.areaName = areaName;
    }

    public final String getCity() {
        return city;
    }

    public final void setCity(final String city) {
        this.city = city;
    }

    public final String getDistrict() {
        return district;
    }

    public final void setDistrict(final String district) {
        this.district = district;
    }

    public final String getMunicipality() {
        return municipality;
    }

    public final void setMunicipality(final String municipality) {
        this.municipality = municipality;
    }

    public final String getRegion() {
        return region;
    }

    public final void setRegion(final String region) {
        this.region = region;
    }

    public final String getCountry() {
        return country;
    }

    public final void setCountry(final String country) {
        this.country = country;
    }

    public final Set<String> getPostalCodes() {
        return postalCodes;
    }

    public final void setPostalCodes(final Set<String> postalCodes) {
        this.postalCodes = postalCodes;
    }
}

