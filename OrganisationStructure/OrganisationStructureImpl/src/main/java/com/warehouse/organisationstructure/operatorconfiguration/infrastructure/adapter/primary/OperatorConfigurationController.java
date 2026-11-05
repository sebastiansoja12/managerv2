package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationOverrideDto;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.mapper.DepartmentConfigurationOverrideApiMapper;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.organisationstructure.api.dto.CommunicationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.FleetConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.PickupPointConfigurationDto;
import com.warehouse.organisationstructure.api.dto.RegionalConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentDeliveryConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentReturnConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentPaymentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentSlaConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentDataRetentionConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentValidationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentWorkflowConfigurationDto;
import com.warehouse.organisationstructure.api.dto.SortingConfigurationDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberRuleDto;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.warehouse.organisationstructure.operatorconfiguration.domain.exception.DepartmentConfigurationAccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operator-configurations")
public class OperatorConfigurationController {

    private final OperatorConfigurationPort operatorConfigurationPort;

    private final DepartmentConfigurationOverrideApiMapper departmentConfigurationOverrideApiMapper =
            new DepartmentConfigurationOverrideApiMapper();

    public OperatorConfigurationController(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @GetMapping
    public ResponseEntity<OperatorConfigurationDto> getCurrent() {
        return ResponseEntity.of(operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoConfiguration));
    }

    @GetMapping("/shipment")
    public ResponseEntity<ShipmentConfigurationDto> getCurrentShipmentConfiguration() {
        return ResponseEntity.of(operatorConfigurationPort.getCurrent()
                .map(configuration -> OperatorMapper.toDtoShipmentConfiguration(
                        configuration.getShipmentConfiguration())));
    }

    @PutMapping("/shipment")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentConfiguration(
            @RequestBody final ShipmentConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoShipmentConfiguration(
                operatorConfigurationPort.updateCurrentShipmentConfiguration(
                        OperatorMapper.toModelShipmentConfiguration(request)).getShipmentConfiguration()));
    }

    @GetMapping("/departments")
    public ResponseEntity<DepartmentConfigurationDto> getCurrentDepartmentConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoDepartmentConfiguration(
                operatorConfigurationPort.getCurrentDepartmentConfiguration()));
    }

    @PutMapping("/departments")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<DepartmentConfigurationDto> updateCurrentDepartmentConfiguration(
            @RequestBody final DepartmentConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoDepartmentConfiguration(
                operatorConfigurationPort.updateCurrentDepartmentConfiguration(
                        OperatorMapper.toModelDepartmentConfiguration(request))));
    }

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<DepartmentConfigurationDto> getDepartmentConfiguration(
            @PathVariable final Long departmentId) {
        return ResponseEntity.ok(OperatorMapper.toDtoDepartmentConfiguration(
                operatorConfigurationPort.getDepartmentConfiguration(new DepartmentId(departmentId))));
    }

    @GetMapping("/departments/{departmentId}/override")
    public ResponseEntity<DepartmentConfigurationOverrideDto> getDepartmentConfigurationOverride(
            @PathVariable final Long departmentId) {
        return ResponseEntity.ok(departmentConfigurationOverrideApiMapper.toDto(
                operatorConfigurationPort.getDepartmentConfigurationOverride(new DepartmentId(departmentId))));
    }

    @PutMapping("/departments/{departmentId}/override")
    @AccessUserControl(minimumLevel = AccountLevel.ADMIN)
    public ResponseEntity<DepartmentConfigurationOverrideDto> updateDepartmentConfigurationOverride(
            @PathVariable final Long departmentId,
            @RequestBody final DepartmentConfigurationOverrideDto request) {
        return ResponseEntity.ok(departmentConfigurationOverrideApiMapper.toDto(
                operatorConfigurationPort.updateDepartmentConfigurationOverride(
                        departmentConfigurationOverrideApiMapper.toModel(new DepartmentId(departmentId), request))));
    }

    @GetMapping("/fleet")
    public ResponseEntity<FleetConfigurationDto> getCurrentFleetConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoFleetConfiguration(operatorConfigurationPort.getCurrentFleetConfiguration()));
    }

    @PutMapping("/fleet")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<FleetConfigurationDto> updateCurrentFleetConfiguration(@RequestBody final FleetConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoFleetConfiguration(
                operatorConfigurationPort.updateCurrentFleetConfiguration(OperatorMapper.toModelFleetConfiguration(request))));
    }

    @GetMapping("/communication")
    public ResponseEntity<CommunicationConfigurationDto> getCurrentCommunicationConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoCommunicationConfiguration(operatorConfigurationPort.getCurrentCommunicationConfiguration()));
    }

    @PutMapping("/communication")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<CommunicationConfigurationDto> updateCurrentCommunicationConfiguration(@RequestBody final CommunicationConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoCommunicationConfiguration(
                operatorConfigurationPort.updateCurrentCommunicationConfiguration(OperatorMapper.toModelCommunicationConfiguration(request))));
    }

    @GetMapping("/regional")
    public ResponseEntity<RegionalConfigurationDto> getCurrentRegionalConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoRegionalConfiguration(operatorConfigurationPort.getCurrentRegionalConfiguration()));
    }

    @PutMapping("/regional")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<RegionalConfigurationDto> updateCurrentRegionalConfiguration(@RequestBody final RegionalConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoRegionalConfiguration(
                operatorConfigurationPort.updateCurrentRegionalConfiguration(OperatorMapper.toModelRegionalConfiguration(request))));
    }

    @GetMapping("/sorting")
    public ResponseEntity<SortingConfigurationDto> getCurrentSortingConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoSortingConfiguration(operatorConfigurationPort.getCurrentSortingConfiguration()));
    }

    @PutMapping("/sorting")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<SortingConfigurationDto> updateCurrentSortingConfiguration(@RequestBody final SortingConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoSortingConfiguration(
                operatorConfigurationPort.updateCurrentSortingConfiguration(OperatorMapper.toModelSortingConfiguration(request))));
    }

    @GetMapping("/pickup-points")
    public ResponseEntity<PickupPointConfigurationDto> getCurrentPickupPointConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoPickupPointConfiguration(operatorConfigurationPort.getCurrentPickupPointConfiguration()));
    }

    @PutMapping("/pickup-points")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<PickupPointConfigurationDto> updateCurrentPickupPointConfiguration(@RequestBody final PickupPointConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoPickupPointConfiguration(
                operatorConfigurationPort.updateCurrentPickupPointConfiguration(OperatorMapper.toModelPickupPointConfiguration(request))));
    }

    @GetMapping("/shipment/limits")
    public ResponseEntity<ShipmentLimitsDto> getCurrentShipmentLimits() {
        return ResponseEntity.of(operatorConfigurationPort.getCurrent()
                .map(OperatorMapper::toDtoCurrentShipmentLimits));
    }

    @PutMapping("/shipment/validation")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentValidationConfiguration(
            @RequestBody final ShipmentValidationConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentValidationConfiguration(
                OperatorMapper.toModelShipmentValidationConfiguration(request)));
    }

    @PutMapping("/shipment/labels")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentLabelConfiguration(
            @RequestBody final ShipmentLabelConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentLabelConfiguration(
                OperatorMapper.toModelShipmentLabelConfiguration(request)));
    }

    @PutMapping("/shipment/limits")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentLimits(
            @RequestBody final ShipmentLimitsDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentLimits(
                OperatorMapper.toModelShipmentLimits(request)));
    }

    @PutMapping("/shipment/workflow")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentWorkflowConfiguration(
            @RequestBody final ShipmentWorkflowConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentWorkflowConfiguration(
                OperatorMapper.toModelShipmentWorkflowConfiguration(request)));
    }

    @PutMapping("/shipment/tracking-number-rule")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentTrackingNumberRule(
            @RequestBody final TrackingNumberRuleDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentTrackingNumberRule(
                OperatorMapper.toModelTrackingNumberRule(request)));
    }

    @PutMapping("/shipment/notifications")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentNotificationConfiguration(
            @RequestBody final ShipmentNotificationConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentNotificationConfiguration(
                OperatorMapper.toModelShipmentNotificationConfiguration(request)));
    }

    @PutMapping("/shipment/delivery")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentDeliveryConfiguration(
            @RequestBody final ShipmentDeliveryConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentDeliveryConfiguration(
                OperatorMapper.toModelShipmentDeliveryConfiguration(request)));
    }

    @PutMapping("/shipment/returns")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentReturnConfiguration(
            @RequestBody final ShipmentReturnConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentReturnConfiguration(
                OperatorMapper.toModelShipmentReturnConfiguration(request)));
    }

    @PutMapping("/shipment/payments")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentPaymentConfiguration(
            @RequestBody final ShipmentPaymentConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentPaymentConfiguration(
                OperatorMapper.toModelShipmentPaymentConfiguration(request)));
    }

    @PutMapping("/shipment/sla")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentSlaConfiguration(
            @RequestBody final ShipmentSlaConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentSlaConfiguration(
                OperatorMapper.toModelShipmentSlaConfiguration(request)));
    }

    @PutMapping("/shipment/data-retention")
    @AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentDataRetentionConfiguration(
            @RequestBody final ShipmentDataRetentionConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentDataRetentionConfiguration(
                OperatorMapper.toModelShipmentDataRetentionConfiguration(request)));
    }

    private ResponseEntity<ShipmentConfigurationDto> shipmentConfigurationResponse(
            final ShipmentConfiguration shipmentConfiguration) {
        return ResponseEntity.ok(OperatorMapper.toDtoShipmentConfiguration(shipmentConfiguration));
    }

    @ExceptionHandler(DepartmentConfigurationAccessDeniedException.class)
    public ResponseEntity<String> handleDepartmentAccessDenied(final DepartmentConfigurationAccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
