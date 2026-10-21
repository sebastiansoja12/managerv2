package com.warehouse.departmentmap.infrastructure.adapter.secondary.mapper;

import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.MapElement;
import com.warehouse.departmentmap.domain.vo.ElementGeometry;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.DepartmentMapEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.MapElementEntity;

import java.util.List;
import java.util.stream.IntStream;

public final class DepartmentMapEntityMapper {

    private DepartmentMapEntityMapper() {
    }

    public static DepartmentMapEntity toEntity(final DepartmentMap departmentMap) {
        final List<MapElement> elements = departmentMap.getElements();
        final DepartmentMapEntity entity = new DepartmentMapEntity(departmentMap.getDepartmentMapId(),
                departmentMap.getDepartmentId(), departmentMap.getName(), departmentMap.getWidth(),
                departmentMap.getHeight(), departmentMap.getGridSize(), departmentMap.getVersion(),
                departmentMap.getCreatedAt(), departmentMap.getUpdatedAt(),
                IntStream.range(0, elements.size())
                        .mapToObj(index -> toEntity(departmentMap, elements.get(index), index))
                        .toList());
        entity.assignOperator(departmentMap.getOperatorId());
        return entity;
    }

    public static DepartmentMap toModel(final DepartmentMapEntity entity) {
        return new DepartmentMap(entity.getDepartmentMapId(), entity.operatorId(), entity.getDepartmentId(),
                entity.getName(), entity.getWidth(), entity.getHeight(), entity.getGridSize(),
                entity.getElements().stream().map(DepartmentMapEntityMapper::toModel).toList(),
                entity.getVersion(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private static MapElementEntity toEntity(final DepartmentMap departmentMap, final MapElement element,
                                             final int sortOrder) {
        final ElementGeometry geometry = element.getGeometry();
        return new MapElementEntity(element.getElementId(), departmentMap.getDepartmentMapId(), element.getType(),
                element.getLabel(), geometry.x(), geometry.y(), geometry.width(), geometry.height(),
                geometry.rotation(), element.getColor(), element.getLocationCode(), element.getCapacity(), sortOrder);
    }

    private static MapElement toModel(final MapElementEntity entity) {
        return new MapElement(entity.getElementId(), entity.getType(), entity.getLabel(),
                new ElementGeometry(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(),
                        entity.getRotation()),
                entity.getColor(), entity.getLocationCode(), entity.getCapacity());
    }
}
