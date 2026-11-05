package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DepartmentConfigurationOverrideTest {

    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(5L);

    @Test
    void shouldInheritEveryValueWhenNothingIsOverridden() {
        final DepartmentConfiguration defaults = DepartmentConfiguration.defaultConfiguration();
        final DepartmentConfigurationOverride override = DepartmentConfigurationOverride.empty(DEPARTMENT_ID);

        final DepartmentConfiguration effective = override.applyTo(defaults);

        assertThat(override.isEmpty()).isTrue();
        assertThat(effective.isPreventDepartmentDeletion()).isEqualTo(defaults.isPreventDepartmentDeletion());
        assertThat(effective.getDeliveryAreaPolicy().getDeliveryAreaMode())
                .isEqualTo(defaults.getDeliveryAreaPolicy().getDeliveryAreaMode());
        assertThat(effective.getConnectionPolicy().getMaxAutoConnectionDistanceKm())
                .isEqualTo(defaults.getConnectionPolicy().getMaxAutoConnectionDistanceKm());
        assertThat(effective.getLifecyclePolicy().getWorkTransferOnDeactivation())
                .isEqualTo(defaults.getLifecyclePolicy().getWorkTransferOnDeactivation());
        assertThat(effective.getCapacityPolicy().getDefaultDailyCapacity())
                .isEqualTo(defaults.getCapacityPolicy().getDefaultDailyCapacity());
    }

    @Test
    void shouldReplaceOnlyOverriddenValues() {
        final DepartmentConfiguration defaults = DepartmentConfiguration.defaultConfiguration();
        final DepartmentConfigurationOverride override = new DepartmentConfigurationOverride(DEPARTMENT_ID,
                null, true, null, true, DeliveryAreaMode.RADIUS, 20, null, null, null, 2, null, null, false, true,
                null, DepartmentWorkTransferMode.AUTOMATIC, 30, 250, null, true, 4);

        final DepartmentConfiguration effective = override.applyTo(defaults);

        assertThat(override.isEmpty()).isFalse();
        assertThat(effective.isAutoAssignDeliveryAreas()).isEqualTo(defaults.isAutoAssignDeliveryAreas());
        assertThat(effective.isAutoConnectDepartments()).isTrue();
        assertThat(effective.isPreventDepartmentArchiving()).isTrue();
        assertThat(effective.getDeliveryAreaPolicy().getDeliveryAreaMode()).isEqualTo(DeliveryAreaMode.RADIUS);
        assertThat(effective.getDeliveryAreaPolicy().getMaxDeliveryRadiusKm()).isEqualTo(20);
        assertThat(effective.getDeliveryAreaPolicy().isAllowManualDeliveryAreaEdit())
                .isEqualTo(defaults.getDeliveryAreaPolicy().isAllowManualDeliveryAreaEdit());
        assertThat(effective.getConnectionPolicy().getSortingCenterConnectionCount()).isEqualTo(2);
        assertThat(effective.getConnectionPolicy().isBidirectionalConnectionsByDefault()).isFalse();
        assertThat(effective.getLifecyclePolicy().isRequireStatusChangeReason()).isTrue();
        assertThat(effective.getLifecyclePolicy().getWorkTransferOnDeactivation())
                .isEqualTo(DepartmentWorkTransferMode.AUTOMATIC);
        assertThat(effective.getLifecyclePolicy().getAutoArchiveInactiveAfterDays()).isEqualTo(30);
        assertThat(effective.getCapacityPolicy().getDefaultDailyCapacity()).isEqualTo(250);
        assertThat(effective.getCapacityPolicy().getCapacityWarningThresholdPercent())
                .isEqualTo(defaults.getCapacityPolicy().getCapacityWarningThresholdPercent());
        assertThat(effective.getCapacityPolicy().isRedirectOverflowToNeighbour()).isTrue();
        assertThat(effective.getCapacityPolicy().getMaxCouriersPerDepartment()).isEqualTo(4);
    }
}
