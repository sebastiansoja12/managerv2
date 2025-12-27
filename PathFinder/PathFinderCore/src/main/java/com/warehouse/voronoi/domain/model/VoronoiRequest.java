package com.warehouse.voronoi.domain.model;

import com.warehouse.voronoi.VoronoiRequestDto;

import java.util.List;

public class VoronoiRequest {
    private String city;
    private String zipCode;
    private List<Department> departments;

    public VoronoiRequest(final String city, final String zipCode, final List<Department> departments) {
        this.city = city;
        this.zipCode = zipCode;
        this.departments = departments;
    }

    public static VoronoiRequest from(final VoronoiRequestDto voronoiRequest) {
        return new VoronoiRequest(voronoiRequest.city(), voronoiRequest.zipCode(), voronoiRequest.departments()
                .stream().map(dep -> new Department(dep.city(), dep.street(), dep.country(), dep.departmentCode(),
                        new Coordinates(dep.coordinates().latitude(), dep.coordinates().longitude())))
                .toList());
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

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(final List<Department> departments) {
        this.departments = departments;
    }
}
