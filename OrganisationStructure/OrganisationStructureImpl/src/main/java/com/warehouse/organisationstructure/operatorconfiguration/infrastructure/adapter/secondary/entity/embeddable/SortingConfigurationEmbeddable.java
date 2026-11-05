package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DuplicateScanPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ScanErrorFeedback;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SortingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UnresolvedDestinationPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class SortingConfigurationEmbeddable {

    @Column(name = "sorting_require_inbound_scan")
    private boolean requireInboundScan;

    @Column(name = "sorting_require_sort_scan")
    private boolean requireSortScan;

    @Column(name = "sorting_require_outbound_scan")
    private boolean requireOutboundScan;

    @Enumerated(EnumType.STRING)
    @Column(name = "sorting_duplicate_scan_policy")
    private DuplicateScanPolicy duplicateScanPolicy;

    @Enumerated(EnumType.STRING)
    @Column(name = "sorting_unresolved_destination_policy")
    private UnresolvedDestinationPolicy unresolvedDestinationPolicy;

    @Column(name = "sorting_exception_ramp_code")
    private String exceptionRampCode;

    @Column(name = "sorting_require_damage_photo_on_inbound")
    private boolean requireDamagePhotoOnInbound;

    @Enumerated(EnumType.STRING)
    @Column(name = "sorting_scan_error_feedback")
    private ScanErrorFeedback scanErrorFeedback;

    @Column(name = "sorting_missing_in_warehouse_after_hours")
    private int missingInWarehouseAfterHours;

    public SortingConfigurationEmbeddable() {
    }

    public static SortingConfigurationEmbeddable from(final SortingConfiguration configuration) {
        final SortingConfiguration source = configuration != null
                ? configuration
                : SortingConfiguration.defaultConfiguration();
        final SortingConfigurationEmbeddable embeddable = new SortingConfigurationEmbeddable();
        embeddable.requireInboundScan = source.isRequireInboundScan();
        embeddable.requireSortScan = source.isRequireSortScan();
        embeddable.requireOutboundScan = source.isRequireOutboundScan();
        embeddable.duplicateScanPolicy = source.getDuplicateScanPolicy();
        embeddable.unresolvedDestinationPolicy = source.getUnresolvedDestinationPolicy();
        embeddable.exceptionRampCode = source.getExceptionRampCode();
        embeddable.requireDamagePhotoOnInbound = source.isRequireDamagePhotoOnInbound();
        embeddable.scanErrorFeedback = source.getScanErrorFeedback();
        embeddable.missingInWarehouseAfterHours = source.getMissingInWarehouseAfterHours();
        return embeddable;
    }

    public SortingConfiguration toModel() {
        return new SortingConfiguration(
                requireInboundScan,
                requireSortScan,
                requireOutboundScan,
                duplicateScanPolicy,
                unresolvedDestinationPolicy,
                exceptionRampCode,
                requireDamagePhotoOnInbound,
                scanErrorFeedback,
                missingInWarehouseAfterHours
        );
    }
}
