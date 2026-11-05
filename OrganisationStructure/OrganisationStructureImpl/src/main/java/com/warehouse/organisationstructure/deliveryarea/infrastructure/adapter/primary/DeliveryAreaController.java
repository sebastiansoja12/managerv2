package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPort;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.DeliveryAreaApiRequest;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.DeliveryAreaApiResponse;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.DepartmentDeliveryAreasApiResponse;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.GenerateDeliveryAreasApiRequest;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api.ReplaceDeliveryAreasApiRequest;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.mapper.DeliveryAreaApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/delivery-areas")
@AccessUserControl
public class DeliveryAreaController {

    private final DeliveryAreaPort deliveryAreaPort;

    private final DeliveryAreaApiMapper deliveryAreaApiMapper;

    public DeliveryAreaController(final DeliveryAreaPort deliveryAreaPort,
                                  final DeliveryAreaApiMapper deliveryAreaApiMapper) {
        this.deliveryAreaPort = deliveryAreaPort;
        this.deliveryAreaApiMapper = deliveryAreaApiMapper;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryAreaApiResponse>> findAll() {
        return ResponseEntity.ok(deliveryAreaApiMapper.map(deliveryAreaPort.findAll()));
    }

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<List<DeliveryAreaApiResponse>> findByDepartment(@PathVariable final Long departmentId) {
        return ResponseEntity.ok(deliveryAreaApiMapper.map(
                deliveryAreaPort.findByDepartment(deliveryAreaApiMapper.mapDepartmentId(departmentId))));
    }

    @PostMapping("/departments/{departmentId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<DeliveryAreaApiResponse> create(@PathVariable final Long departmentId,
                                                        @Valid @RequestBody final DeliveryAreaApiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryAreaApiMapper.map(
                deliveryAreaPort.create(deliveryAreaApiMapper.mapCreate(departmentId, request))));
    }

    @PutMapping("/departments/{departmentId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<List<DeliveryAreaApiResponse>> replaceDepartmentAreas(
            @PathVariable final Long departmentId,
            @Valid @RequestBody final ReplaceDeliveryAreasApiRequest request) {
        return ResponseEntity.ok(deliveryAreaApiMapper.map(
                deliveryAreaPort.replaceDepartmentAreas(deliveryAreaApiMapper.map(departmentId, request))));
    }

    @PutMapping("/{deliveryAreaId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<DeliveryAreaApiResponse> update(@PathVariable final Long deliveryAreaId,
                                                           @Valid @RequestBody final DeliveryAreaApiRequest request) {
        return ResponseEntity.ok(deliveryAreaApiMapper.map(
                deliveryAreaPort.update(deliveryAreaApiMapper.mapUpdate(deliveryAreaId, request))));
    }

    @DeleteMapping("/{deliveryAreaId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<Void> delete(@PathVariable final Long deliveryAreaId) {
        deliveryAreaPort.delete(deliveryAreaApiMapper.mapDeliveryAreaId(deliveryAreaId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/automatic/previews")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<List<DepartmentDeliveryAreasApiResponse>> previewAutomatic(
            @Valid @RequestBody final GenerateDeliveryAreasApiRequest request) {
        return ResponseEntity.ok(deliveryAreaApiMapper.mapDepartments(
                deliveryAreaPort.previewAutomatic(deliveryAreaApiMapper.map(request))));
    }

    @PostMapping("/automatic")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<List<DepartmentDeliveryAreasApiResponse>> applyAutomatic(
            @Valid @RequestBody final GenerateDeliveryAreasApiRequest request) {
        return ResponseEntity.ok(deliveryAreaApiMapper.mapDepartments(
                deliveryAreaPort.applyAutomatic(deliveryAreaApiMapper.map(request))));
    }
}
