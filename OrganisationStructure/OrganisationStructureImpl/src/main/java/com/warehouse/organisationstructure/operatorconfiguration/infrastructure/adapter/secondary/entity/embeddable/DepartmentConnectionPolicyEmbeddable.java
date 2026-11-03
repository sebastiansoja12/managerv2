package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConnectionPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DepartmentConnectionPolicyEmbeddable {

    @Column(name = "department_sorting_center_connection_count")
    private int sortingCenterConnectionCount;

    @Column(name = "department_max_auto_connection_distance_km")
    private int maxAutoConnectionDistanceKm;

    @Column(name = "department_require_sorting_connection_for_activation")
    private boolean requireSortingConnectionForActivation;

    @Column(name = "department_bidirectional_connections_by_default")
    private boolean bidirectionalConnectionsByDefault;

    public DepartmentConnectionPolicyEmbeddable() {
    }

    public static DepartmentConnectionPolicyEmbeddable from(final DepartmentConnectionPolicy configuration) {
        final DepartmentConnectionPolicy source = configuration != null
                ? configuration
                : DepartmentConnectionPolicy.defaultConfiguration();
        final DepartmentConnectionPolicyEmbeddable embeddable = new DepartmentConnectionPolicyEmbeddable();
        embeddable.sortingCenterConnectionCount = source.getSortingCenterConnectionCount();
        embeddable.maxAutoConnectionDistanceKm = source.getMaxAutoConnectionDistanceKm();
        embeddable.requireSortingConnectionForActivation = source.isRequireSortingConnectionForActivation();
        embeddable.bidirectionalConnectionsByDefault = source.isBidirectionalConnectionsByDefault();
        return embeddable;
    }

    public DepartmentConnectionPolicy toModel() {
        return new DepartmentConnectionPolicy(
                sortingCenterConnectionCount,
                maxAutoConnectionDistanceKm,
                requireSortingConnectionForActivation,
                bidirectionalConnectionsByDefault
        );
    }
}
