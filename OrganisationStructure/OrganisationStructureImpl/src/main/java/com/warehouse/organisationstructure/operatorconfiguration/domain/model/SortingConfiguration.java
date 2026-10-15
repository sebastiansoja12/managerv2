package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class SortingConfiguration {

    private final boolean requireInboundScan;
    private final boolean requireSortScan;
    private final boolean requireOutboundScan;
    private final DuplicateScanPolicy duplicateScanPolicy;
    private final UnresolvedDestinationPolicy unresolvedDestinationPolicy;
    private final String exceptionRampCode;
    private final boolean requireDamagePhotoOnInbound;
    private final ScanErrorFeedback scanErrorFeedback;
    private final int missingInWarehouseAfterHours;

    public SortingConfiguration(final boolean requireInboundScan,
                                final boolean requireSortScan,
                                final boolean requireOutboundScan,
                                final DuplicateScanPolicy duplicateScanPolicy,
                                final UnresolvedDestinationPolicy unresolvedDestinationPolicy,
                                final String exceptionRampCode,
                                final boolean requireDamagePhotoOnInbound,
                                final ScanErrorFeedback scanErrorFeedback,
                                final int missingInWarehouseAfterHours) {
        this.requireInboundScan = requireInboundScan;
        this.requireSortScan = requireSortScan;
        this.requireOutboundScan = requireOutboundScan;
        this.duplicateScanPolicy = duplicateScanPolicy;
        this.unresolvedDestinationPolicy = unresolvedDestinationPolicy;
        this.exceptionRampCode = exceptionRampCode;
        this.requireDamagePhotoOnInbound = requireDamagePhotoOnInbound;
        this.scanErrorFeedback = scanErrorFeedback;
        this.missingInWarehouseAfterHours = missingInWarehouseAfterHours;
    }

    public static SortingConfiguration defaultConfiguration() {
        return new SortingConfiguration(
                true,
                true,
                true,
                DuplicateScanPolicy.WARN,
                UnresolvedDestinationPolicy.ROUTE_TO_EXCEPTION_RAMP,
                "",
                true,
                ScanErrorFeedback.SOUND_AND_VIBRATION,
                24
        );
    }

    public boolean isRequireInboundScan() { return requireInboundScan; }
    public boolean isRequireSortScan() { return requireSortScan; }
    public boolean isRequireOutboundScan() { return requireOutboundScan; }
    public DuplicateScanPolicy getDuplicateScanPolicy() { return duplicateScanPolicy; }
    public UnresolvedDestinationPolicy getUnresolvedDestinationPolicy() { return unresolvedDestinationPolicy; }
    public String getExceptionRampCode() { return exceptionRampCode; }
    public boolean isRequireDamagePhotoOnInbound() { return requireDamagePhotoOnInbound; }
    public ScanErrorFeedback getScanErrorFeedback() { return scanErrorFeedback; }
    public int getMissingInWarehouseAfterHours() { return missingInWarehouseAfterHours; }
}
