package com.warehouse.department.domain.port.primary;

import java.util.List;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepotCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;

public interface DepotPort {

    Department viewDepotByCode(DepotCode depotCode);

    List<Department> findAll();

    void addMultipleDepots(List<Department> departments);

    void updateStreet(UpdateStreetRequest request);
}
