package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.api.dto.DepartmentDto;

public class Department {
    private DepartmentCode departmentCode;
    private Boolean active;
    private Double latitude;
    private Double longitude;

    public Department(final DepartmentCode departmentCode, final Boolean active) {
        this.departmentCode = departmentCode;
        this.active = active;
    }

    public Department(final DepartmentCode departmentCode, final Double latitude, final Double longitude) {
        this.departmentCode = departmentCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Department from(final DepartmentDto department) {
        return new Department(new DepartmentCode(department.departmentCode()), department.coordinates().latitude(), department.coordinates().longitude());
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Coordinates getCoordinates() {
        return new Coordinates(latitude, longitude);
    }

    public class Coordinates {
        private Double latitude;
        private Double longitude;

        public Coordinates(final Double latitude, final Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }
    }
}
