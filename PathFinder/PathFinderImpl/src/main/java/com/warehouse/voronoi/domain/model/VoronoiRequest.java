package com.warehouse.voronoi.domain.model;

import java.util.List;

public class VoronoiRequest {
    private String city;
    private Coordinates coordinates;
    private List<Department> departments;

    public VoronoiRequest(final String city,
                          final Coordinates coordinates,
                          final List<Department> departments) {
        this.city = city;
        this.coordinates = coordinates;
        this.departments = departments;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(final Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(final List<Department> departments) {
        this.departments = departments;
    }
}
