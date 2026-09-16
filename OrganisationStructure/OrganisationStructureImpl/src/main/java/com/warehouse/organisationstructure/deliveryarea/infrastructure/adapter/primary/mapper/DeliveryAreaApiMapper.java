package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.CreateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.GenerateDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.ReplaceDepartmentDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.UpdateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.result.DepartmentDeliveryAreas;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDraft;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.DeliveryAreaApiRequest;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.DeliveryAreaApiResponse;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.DepartmentDeliveryAreasApiResponse;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.GenerateDeliveryAreasApiRequest;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.GeoPointApi;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.ReplaceDeliveryAreasApiRequest;

import java.util.List;

public class DeliveryAreaApiMapper {

    public DepartmentId mapDepartmentId(final Long departmentId) {
        return new DepartmentId(departmentId);
    }

    public DeliveryAreaId mapDeliveryAreaId(final Long deliveryAreaId) {
        return new DeliveryAreaId(deliveryAreaId);
    }

    public CreateDeliveryAreaCommand mapCreate(final Long departmentId, final DeliveryAreaApiRequest request) {
        return new CreateDeliveryAreaCommand(mapDepartmentId(departmentId), new DeliveryAreaName(request.name()),
                mapBoundary(request.points()));
    }

    public UpdateDeliveryAreaCommand mapUpdate(final Long deliveryAreaId, final DeliveryAreaApiRequest request) {
        return new UpdateDeliveryAreaCommand(mapDeliveryAreaId(deliveryAreaId), new DeliveryAreaName(request.name()),
                mapBoundary(request.points()));
    }

    public ReplaceDepartmentDeliveryAreasCommand map(final Long departmentId,
                                                     final ReplaceDeliveryAreasApiRequest request) {
        return new ReplaceDepartmentDeliveryAreasCommand(mapDepartmentId(departmentId), request.areas().stream()
                .map(area -> new DeliveryAreaDraft(
                        area.deliveryAreaId() != null ? mapDeliveryAreaId(area.deliveryAreaId()) : null,
                        new DeliveryAreaName(area.name()),
                        mapBoundary(area.points())))
                .toList());
    }

    public GenerateDeliveryAreasCommand map(final GenerateDeliveryAreasApiRequest request) {
        return new GenerateDeliveryAreasCommand(request.departmentIds().stream().map(this::mapDepartmentId).toList());
    }

    public DeliveryAreaApiResponse map(final DeliveryArea deliveryArea) {
        return new DeliveryAreaApiResponse(
                deliveryArea.getDeliveryAreaId().value(),
                deliveryArea.getDepartmentId().getValue(),
                deliveryArea.getName().value(),
                deliveryArea.getSource().name(),
                deliveryArea.getBoundary().points().stream()
                        .map(point -> new GeoPointApi(point.latitude(), point.longitude()))
                        .toList(),
                deliveryArea.getUpdatedAt());
    }

    public List<DeliveryAreaApiResponse> map(final List<DeliveryArea> deliveryAreas) {
        return deliveryAreas.stream().map(this::map).toList();
    }

    public List<DepartmentDeliveryAreasApiResponse> mapDepartments(final List<DepartmentDeliveryAreas> departments) {
        return departments.stream()
                .map(department -> new DepartmentDeliveryAreasApiResponse(department.departmentId().getValue(),
                        department.departmentCode().getValue(), map(department.areas())))
                .toList();
    }

    private DeliveryAreaBoundary mapBoundary(final List<GeoPointApi> points) {
        return new DeliveryAreaBoundary(points.stream()
                .map(point -> new GeoPoint(point.latitude(), point.longitude()))
                .toList());
    }
}
