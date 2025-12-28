package com.warehouse.process.domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;

public class CommunicationLogDetails {
    private Set<CommunicationLogDetail> communicationLogDetails;

    public Set<CommunicationLogDetail> getCommunicationLogDetails() {
        if (communicationLogDetails == null) {
            this.communicationLogDetails = new HashSet<>();
        }
        return communicationLogDetails;
    }

    public CommunicationLogDetail getCommunicationLogDetail(final ProcessType processType,
                                                            final ServiceType serviceType) {
        return getCommunicationLogDetails()
                .stream()
                .filter(equalProcessType(processType))
                .findFirst()
                .orElseGet(() -> addNewCommunicationLog(processType, serviceType));
    }

    private CommunicationLogDetail addNewCommunicationLog(final ProcessType processType, final ServiceType serviceType) {
        final CommunicationLogDetail communicationLogDetail = createNewCommunicationLogDetail(processType, serviceType);
        getCommunicationLogDetails().add(communicationLogDetail);
        return communicationLogDetail;
    }

    private CommunicationLogDetail createNewCommunicationLogDetail(final ProcessType processType,
                                                                   final ServiceType serviceType) {
        return CommunicationLogDetail
                .builder()
                .serviceType(serviceType)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .processType(processType)
                .build();
    }

    private Predicate<? super CommunicationLogDetail> equalRouteLogRecordDetailId(final Long id) {
        return communicationLogDetail -> Objects.equals(id, communicationLogDetail.getId());
    }

    private Predicate<? super CommunicationLogDetail> equalProcessType(final ProcessType processType) {
        return communicationLogDetail -> processType.equals(communicationLogDetail.getProcessType());
    }
}
