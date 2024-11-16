package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepotCode;

import java.util.List;

public interface DepartmentRepository {

    void save(final Department department);

    void saveAll(final List<Department> departments);

    Department findByCode(final DepotCode depotCode);

    List<Department> findAll();

}
