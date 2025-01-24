package com.warehouse.pallet.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.TruckId;

import java.util.ArrayList;
import java.util.List;

public class Driver {
    private DriverId driverId;
    private String firstName;
    private String lastName;
    private List<DepartmentCode> assignedDepartments;
    private List<TruckId> assignedTrucks;

    public Driver(final DriverId driverId,
                  final String firstName,
                  final String lastName,
                  final List<DepartmentCode> assignedDepartments,
                  final List<TruckId> assignedTrucks) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assignedDepartments = assignedDepartments;
        this.assignedTrucks = assignedTrucks;
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

    public List<TruckId> getAssignedTrucks() {
        return assignedTrucks;
    }

    public void setAssignedTrucks(final List<TruckId> assignedTrucks) {
        this.assignedTrucks = assignedTrucks;
    }
}
