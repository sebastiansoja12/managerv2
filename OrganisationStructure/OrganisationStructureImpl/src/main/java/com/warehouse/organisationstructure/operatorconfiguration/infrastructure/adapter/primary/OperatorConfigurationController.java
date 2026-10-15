package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operator-configurations")
public class OperatorConfigurationController {

    private final OperatorConfigurationPort operatorConfigurationPort;

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
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<DepartmentConfigurationDto> updateCurrentDepartmentConfiguration(
            @RequestBody final DepartmentConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoDepartmentConfiguration(
                operatorConfigurationPort.updateCurrentDepartmentConfiguration(
                        OperatorMapper.toModelDepartmentConfiguration(request))));
    }

    @GetMapping("/fleet")
    public ResponseEntity<FleetConfigurationDto> getCurrentFleetConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoFleetConfiguration(operatorConfigurationPort.getCurrentFleetConfiguration()));
    }

    @PutMapping("/fleet")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<FleetConfigurationDto> updateCurrentFleetConfiguration(@RequestBody final FleetConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoFleetConfiguration(
                operatorConfigurationPort.updateCurrentFleetConfiguration(OperatorMapper.toModelFleetConfiguration(request))));
    }

    @GetMapping("/communication")
    public ResponseEntity<CommunicationConfigurationDto> getCurrentCommunicationConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoCommunicationConfiguration(operatorConfigurationPort.getCurrentCommunicationConfiguration()));
    }

    @PutMapping("/communication")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<CommunicationConfigurationDto> updateCurrentCommunicationConfiguration(@RequestBody final CommunicationConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoCommunicationConfiguration(
                operatorConfigurationPort.updateCurrentCommunicationConfiguration(OperatorMapper.toModelCommunicationConfiguration(request))));
    }

    @GetMapping("/regional")
    public ResponseEntity<RegionalConfigurationDto> getCurrentRegionalConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoRegionalConfiguration(operatorConfigurationPort.getCurrentRegionalConfiguration()));
    }

    @PutMapping("/regional")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<RegionalConfigurationDto> updateCurrentRegionalConfiguration(@RequestBody final RegionalConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoRegionalConfiguration(
                operatorConfigurationPort.updateCurrentRegionalConfiguration(OperatorMapper.toModelRegionalConfiguration(request))));
    }

    @GetMapping("/sorting")
    public ResponseEntity<SortingConfigurationDto> getCurrentSortingConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoSortingConfiguration(operatorConfigurationPort.getCurrentSortingConfiguration()));
    }

    @PutMapping("/sorting")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<SortingConfigurationDto> updateCurrentSortingConfiguration(@RequestBody final SortingConfigurationDto request) {
        return ResponseEntity.ok(OperatorMapper.toDtoSortingConfiguration(
                operatorConfigurationPort.updateCurrentSortingConfiguration(OperatorMapper.toModelSortingConfiguration(request))));
    }

    @GetMapping("/pickup-points")
    public ResponseEntity<PickupPointConfigurationDto> getCurrentPickupPointConfiguration() {
        return ResponseEntity.ok(OperatorMapper.toDtoPickupPointConfiguration(operatorConfigurationPort.getCurrentPickupPointConfiguration()));
    }

    @PutMapping("/pickup-points")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
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
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentValidationConfiguration(
            @RequestBody final ShipmentValidationConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentValidationConfiguration(
                OperatorMapper.toModelShipmentValidationConfiguration(request)));
    }

    @PutMapping("/shipment/labels")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentLabelConfiguration(
            @RequestBody final ShipmentLabelConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentLabelConfiguration(
                OperatorMapper.toModelShipmentLabelConfiguration(request)));
    }

    @PutMapping("/shipment/limits")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentLimits(
            @RequestBody final ShipmentLimitsDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentLimits(
                OperatorMapper.toModelShipmentLimits(request)));
    }

    @PutMapping("/shipment/workflow")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentWorkflowConfiguration(
            @RequestBody final ShipmentWorkflowConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentWorkflowConfiguration(
                OperatorMapper.toModelShipmentWorkflowConfiguration(request)));
    }

    @PutMapping("/shipment/tracking-number-rule")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentTrackingNumberRule(
            @RequestBody final TrackingNumberRuleDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentTrackingNumberRule(
                OperatorMapper.toModelTrackingNumberRule(request)));
    }

    @PutMapping("/shipment/notifications")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentNotificationConfiguration(
            @RequestBody final ShipmentNotificationConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentNotificationConfiguration(
                OperatorMapper.toModelShipmentNotificationConfiguration(request)));
    }

    @PutMapping("/shipment/delivery")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentDeliveryConfiguration(
            @RequestBody final ShipmentDeliveryConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentDeliveryConfiguration(
                OperatorMapper.toModelShipmentDeliveryConfiguration(request)));
    }

    @PutMapping("/shipment/returns")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentReturnConfiguration(
            @RequestBody final ShipmentReturnConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentReturnConfiguration(
                OperatorMapper.toModelShipmentReturnConfiguration(request)));
    }

    @PutMapping("/shipment/payments")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentPaymentConfiguration(
            @RequestBody final ShipmentPaymentConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentPaymentConfiguration(
                OperatorMapper.toModelShipmentPaymentConfiguration(request)));
    }

    @PutMapping("/shipment/sla")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentSlaConfiguration(
            @RequestBody final ShipmentSlaConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentSlaConfiguration(
                OperatorMapper.toModelShipmentSlaConfiguration(request)));
    }

    @PutMapping("/shipment/data-retention")
    public ResponseEntity<ShipmentConfigurationDto> updateCurrentShipmentDataRetentionConfiguration(
            @RequestBody final ShipmentDataRetentionConfigurationDto request) {
        return shipmentConfigurationResponse(operatorConfigurationPort.updateCurrentShipmentDataRetentionConfiguration(
                OperatorMapper.toModelShipmentDataRetentionConfiguration(request)));
    }

    private ResponseEntity<ShipmentConfigurationDto> shipmentConfigurationResponse(
            final ShipmentConfiguration shipmentConfiguration) {
        return ResponseEntity.ok(OperatorMapper.toDtoShipmentConfiguration(shipmentConfiguration));
    }
}
