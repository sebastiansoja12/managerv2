package com.warehouse.departmentmap.application.port.primary.command;

import com.warehouse.departmentmap.domain.model.MapElement;

import java.util.List;

public record SaveDepartmentMapLayoutCommand(String name,
                                             double width,
                                             double height,
                                             double gridSize,
                                             Long version,
                                             List<MapElement> elements) {
}
