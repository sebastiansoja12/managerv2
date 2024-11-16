package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepotCode;

import java.util.List;

public interface DepotRepository {

    void save(Department department);

    void saveAll(List<Department> departments);

    Department findByCode(DepotCode depotCode);

    List<Department> findAll();

}
