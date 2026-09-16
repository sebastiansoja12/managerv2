package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaDepartmentServicePort;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDepartment;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import com.warehouse.organisationstructure.department.application.port.primary.DepartmentPort;
import com.warehouse.organisationstructure.department.domain.model.Department;

import java.util.List;
import java.util.Optional;

public class DeliveryAreaDepartmentServiceAdapter implements DeliveryAreaDepartmentServicePort {

    private final DepartmentPort departmentPort;

    public DeliveryAreaDepartmentServiceAdapter(final DepartmentPort departmentPort) {
        this.departmentPort = departmentPort;
    }

    @Override
    public Optional<DeliveryAreaDepartment> findDepartment(final DepartmentId departmentId) {
        return Optional.ofNullable(departmentPort.findByDepartmentId(departmentId)).map(this::map);
    }

    @Override
    public List<DeliveryAreaDepartment> findDepartments() {
        return departmentPort.findAll().stream().map(this::map).toList();
    }

    private DeliveryAreaDepartment map(final Department department) {
        final GeoPoint location = department.getCoordinates() != null
                ? new GeoPoint(department.getCoordinates().lat(), department.getCoordinates().lon())
                : null;
        return new DeliveryAreaDepartment(department.getDepartmentId(), department.getDepartmentCode(),
                department.getStatus() == Department.Status.ACTIVE, department.getCountryCode(), location);
    }
}
