package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberRule;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;

import java.util.Optional;

public interface OperatorConfigurationPort {

    Optional<OperatorConfiguration> getCurrent();

    OperatorConfiguration updateCurrentShipmentConfiguration(final ShipmentConfiguration shipmentConfiguration);

    DepartmentConfiguration getCurrentDepartmentConfiguration();

    DepartmentConfiguration updateCurrentDepartmentConfiguration(final DepartmentConfiguration departmentConfiguration);

    void create(final OperatorId operatorId, final OperatorConfiguration configuration);

    ShipmentNotificationConfiguration getShipmentNotificationConfiguration(final OperatorId operatorId);

    ShipmentConfiguration updateCurrentShipmentValidationConfiguration(final ShipmentValidationConfiguration validationConfiguration);

    ShipmentConfiguration updateCurrentShipmentLabelConfiguration(final ShipmentLabelConfiguration labelConfiguration);

    ShipmentConfiguration updateCurrentShipmentLimits(final ShipmentLimits shipmentLimits);

    ShipmentConfiguration updateCurrentShipmentWorkflowConfiguration(final ShipmentWorkflowConfiguration workflowConfiguration);

    ShipmentConfiguration updateCurrentTrackingNumberRule(final TrackingNumberRule trackingNumberRule);

    ShipmentConfiguration updateCurrentShipmentNotificationConfiguration(final ShipmentNotificationConfiguration notificationConfiguration);
}
