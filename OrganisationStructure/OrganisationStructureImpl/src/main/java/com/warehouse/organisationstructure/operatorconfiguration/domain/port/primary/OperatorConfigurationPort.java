package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CommunicationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SortingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberRule;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDeliveryConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentReturnConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentPaymentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentSlaConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDataRetentionConfiguration;

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

    ShipmentConfiguration updateCurrentShipmentDeliveryConfiguration(final ShipmentDeliveryConfiguration deliveryConfiguration);

    ShipmentConfiguration updateCurrentShipmentReturnConfiguration(final ShipmentReturnConfiguration returnConfiguration);

    ShipmentConfiguration updateCurrentShipmentPaymentConfiguration(final ShipmentPaymentConfiguration paymentConfiguration);

    ShipmentConfiguration updateCurrentShipmentSlaConfiguration(final ShipmentSlaConfiguration slaConfiguration);

    ShipmentConfiguration updateCurrentShipmentDataRetentionConfiguration(final ShipmentDataRetentionConfiguration dataRetentionConfiguration);

    FleetConfiguration getCurrentFleetConfiguration();

    FleetConfiguration updateCurrentFleetConfiguration(final FleetConfiguration fleetConfiguration);

    CommunicationConfiguration getCurrentCommunicationConfiguration();

    CommunicationConfiguration updateCurrentCommunicationConfiguration(final CommunicationConfiguration communicationConfiguration);

    RegionalConfiguration getCurrentRegionalConfiguration();

    RegionalConfiguration updateCurrentRegionalConfiguration(final RegionalConfiguration regionalConfiguration);

    SortingConfiguration getCurrentSortingConfiguration();

    SortingConfiguration updateCurrentSortingConfiguration(final SortingConfiguration sortingConfiguration);

    PickupPointConfiguration getCurrentPickupPointConfiguration();

    PickupPointConfiguration updateCurrentPickupPointConfiguration(final PickupPointConfiguration pickupPointConfiguration);
}
