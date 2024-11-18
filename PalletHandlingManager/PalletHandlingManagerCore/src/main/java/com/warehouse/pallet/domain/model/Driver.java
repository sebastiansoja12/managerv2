package com.warehouse.pallet.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.pallet.domain.vo.DriverId;

public class Driver {
    private DriverId driverId;
    private String firstName;
    private String lastName;
    private List<DepartmentCode> assignedDepartments;

    public Driver(final DriverId driverId,
                  final String firstName,
                  final String lastName,
                  final List<DepartmentCode> assignedDepartments) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assignedDepartments = assignedDepartments;
    }

    public DriverId getDriverId() {
        return driverId;
    }

    public void setDriverId(final DriverId driverId) {
        this.driverId = driverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public List<DepartmentCode> getAssignedDepartments() {
        if (assignedDepartments == null) {
            assignedDepartments = new ArrayList<>();
        }
        return assignedDepartments;
    }

    public void setAssignedDepartments(final List<DepartmentCode> assignedDepartments) {
        this.assignedDepartments = assignedDepartments;
    }
}
