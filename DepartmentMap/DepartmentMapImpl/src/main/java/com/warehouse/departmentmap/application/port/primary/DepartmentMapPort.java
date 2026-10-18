package com.warehouse.departmentmap.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.departmentmap.application.port.primary.command.CreateDepartmentMapCommand;
import com.warehouse.departmentmap.application.port.primary.command.SaveDepartmentMapLayoutCommand;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapSummary;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapView;

import java.util.List;

public interface DepartmentMapPort {
    List<DepartmentMapSummary> findMaps(final DepartmentId departmentId);
    DepartmentMapView getMap(final DepartmentMapId departmentMapId);
    DepartmentMapView createMap(final CreateDepartmentMapCommand command);
    DepartmentMapView saveLayout(final DepartmentMapId departmentMapId, final SaveDepartmentMapLayoutCommand command);
    void deleteMap(final DepartmentMapId departmentMapId);
}
