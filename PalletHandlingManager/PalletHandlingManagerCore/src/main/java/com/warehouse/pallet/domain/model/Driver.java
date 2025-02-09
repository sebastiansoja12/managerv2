package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.DepartmentCode;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.TruckId;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.DriverDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Driver {
    private DriverId driverId;
    private String firstName;
    private String lastName;
    private List<DepartmentCode> assignedDepartments;
    private Set<TruckId> assignedTrucks;

    public Driver(final DriverId driverId,
                  final String firstName,
                  final String lastName,
                  final List<DepartmentCode> assignedDepartments,
                  final Set<TruckId> assignedTrucks) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assignedDepartments = assignedDepartments;
        this.assignedTrucks = assignedTrucks;
    }

    public Driver() {

    }

    public static Driver empty() {
        return new Driver();
    }

    public static Driver from(final DriverDocument driver) {
        return new Driver(driver.getDriverId(), driver.getFirstName(), driver.getLastName(), driver.getAssignedDepartments(), driver.getAssignedTrucks());
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

    public Set<TruckId> getAssignedTrucks() {
        return assignedTrucks;
    }

    public void setAssignedTrucks(final Set<TruckId> assignedTrucks) {
        this.assignedTrucks = assignedTrucks;
    }
}
