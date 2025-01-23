package com.warehouse.voronoi.domain.model;

import com.warehouse.voronoi.VoronoiRequestDto;

public class VoronoiRequest {
    private String city;
    private String zipCode;

    public VoronoiRequest(final String city, final String zipCode) {
        this.city = city;
        this.zipCode = zipCode;
    }

    public static VoronoiRequest from(final VoronoiRequestDto voronoiRequest) {
        return new VoronoiRequest(voronoiRequest.city(), voronoiRequest.zipCode());
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }
}
