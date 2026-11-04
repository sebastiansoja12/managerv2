package com.warehouse.organisationstructure.deliveryarea.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.CreateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.GenerateDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.ReplaceDepartmentDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.UpdateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.result.DepartmentDeliveryAreas;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;

import java.util.List;
import java.util.Optional;

public interface DeliveryAreaPort {

    List<DeliveryArea> findAll();

    List<DeliveryArea> findByDepartment(final DepartmentId departmentId);

    DeliveryArea create(final CreateDeliveryAreaCommand command);

    DeliveryArea update(final UpdateDeliveryAreaCommand command);

    void delete(final DeliveryAreaId deliveryAreaId);

    List<DeliveryArea> replaceDepartmentAreas(final ReplaceDepartmentDeliveryAreasCommand command);

    List<DepartmentDeliveryAreas> previewAutomatic(final GenerateDeliveryAreasCommand command);

    List<DepartmentDeliveryAreas> applyAutomatic(final GenerateDeliveryAreasCommand command);

    void assignAutomaticallyAfterDepartmentCreated(final DepartmentId departmentId);

    void reassignAfterDepartmentDeleted(final DepartmentId departmentId);

    void recalculateAfterDepartmentStatusChanged(final DepartmentId departmentId);

    Optional<DepartmentId> findServingDepartment(final GeoPoint location);
}
