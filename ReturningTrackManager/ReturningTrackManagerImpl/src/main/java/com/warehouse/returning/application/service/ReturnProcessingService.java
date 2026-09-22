package com.warehouse.returning.application.service;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.returning.domain.event.ReturnPackageProcessingStarted;
import com.warehouse.returning.domain.event.ReturnPackageCanceled;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReturnProcessingService {

    private final ReturnRepository returnRepository;
    private final DomainEventPublisher domainEventPublisher;

    public ReturnProcessingService(final ReturnRepository returnRepository,
                                   final DomainEventPublisher domainEventPublisher) {
        this.returnRepository = returnRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public void startProcessing(final ReturnPackageId returnPackageId) {
        final ReturnPackage returnPackage = returnRepository.findForProcessing(returnPackageId);
        final ReturnPackageProcessingStarted event = returnPackage.markAsProcessing();
        returnRepository.createOrUpdate(returnPackage);
        domainEventPublisher.publish(event);
    }

    @Transactional
    public void cancel(final ReturnPackageId returnPackageId) {
        final ReturnPackage returnPackage = returnRepository.findForProcessing(returnPackageId);
        if (returnPackage.getReturnStatus() == ReturnStatus.CANCELLED) {
            return;
        }
        returnPackage.markAsCanceled();
        returnRepository.createOrUpdate(returnPackage);
        domainEventPublisher.publish(new ReturnPackageCanceled(returnPackage.toSnapshot(), returnPackage.getUpdatedAt()));
    }
}
