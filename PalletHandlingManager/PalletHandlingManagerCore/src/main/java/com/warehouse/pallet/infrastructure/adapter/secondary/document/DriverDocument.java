package com.warehouse.pallet.infrastructure.adapter.secondary.document;

import com.warehouse.pallet.configuration.identificator.DepartmentCode;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.TruckId;

import java.util.List;
import java.util.Set;

public class DriverDocument {
    private DriverId driverId;
    private String firstName;
    private String lastName;
    private List<DepartmentCode> assignedDepartments;
    private Set<TruckId> assignedTrucks;
}
