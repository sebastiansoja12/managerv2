package com.warehouse.organisationstructure.api.dto;

public record SortingConfigurationDto(
        boolean requireInboundScan,
        boolean requireSortScan,
        boolean requireOutboundScan,
        DuplicateScanPolicyDto duplicateScanPolicy,
        UnresolvedDestinationPolicyDto unresolvedDestinationPolicy,
        String exceptionRampCode,
        boolean requireDamagePhotoOnInbound,
        ScanErrorFeedbackDto scanErrorFeedback,
        int missingInWarehouseAfterHours
) {
}
