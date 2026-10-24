package com.warehouse.process.domain.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.context.DomainContext;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.event.ProcessCreatedEvent;
import com.warehouse.process.domain.event.ProcessFinished;
import com.warehouse.process.domain.event.ProcessResponseUpdated;
import com.warehouse.process.domain.exception.ProcessLogNotFoundException;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.domain.vo.DeviceValidation;
import com.warehouse.process.domain.vo.ProcessCommunication;
import com.warehouse.process.domain.vo.ShipmentRejected;
import com.warehouse.process.domain.vo.ShipmentUpdated;

public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;

    private final CurrentUserServicePort currentUserServicePort;

    public ProcessServiceImpl(final ProcessRepository processRepository,
                              final CurrentUserServicePort currentUserServicePort) {
        this.processRepository = processRepository;
        this.currentUserServicePort = currentUserServicePort;
    }

    @Override
    public ProcessId nextProcessId() {
        return new ProcessId(UUID.randomUUID());
    }

    @Override
    public void createProcess(final ProcessLog processLog) {
        this.processRepository.create(processLog);
        DomainContext.eventPublisher().publishEvent(new ProcessCreatedEvent(processLog.snapshot(), Instant.now()));
    }

    @Override
    public ProcessLog findById(final ProcessId processId) {
        return this.processRepository.findById(processId)
                .orElseThrow(() -> new ProcessLogNotFoundException("Process log not found"));
    }

    @Override
    public ProcessLog findByIdForCurrentDepartment(final ProcessId processId) {
        final Optional<ProcessLog> processLog = this.currentUserServicePort.isCurrentUserSupplier()
                ? this.processRepository.findByIdForUser(processId, this.currentUserServicePort.getCurrentUserId())
                : this.processRepository.findByIdForCurrentDepartment(processId);
        return processLog.orElseThrow(() -> new ProcessLogNotFoundException("Process log not found"));
    }

    @Override
    public Page<ProcessLog> findAllForCurrentDepartment(final Pageable pageable) {
        if (this.currentUserServicePort.isCurrentUserSupplier()) {
            return this.processRepository.findAllForUser(pageable, this.currentUserServicePort.getCurrentUserId());
        }
        return this.processRepository.findAllForCurrentDepartment(pageable);
    }

    @Override
    public void assignShipmentUpdated(final ProcessId processId, final ShipmentUpdated shipmentUpdated) {
        this.processRepository.findById(processId)
                .ifPresent(processLog -> {
                    processLog.applyShipmentUpdate(shipmentUpdated);
                    this.processRepository.update(processLog);
                });
    }

    @Override
    public void assignShipmentRejected(final ProcessId processId, final ShipmentRejected shipmentRejected) {
        this.processRepository.findById(processId)
                .ifPresent(processLog -> {
                    processLog.applyShipmentRejected(shipmentRejected);
                    this.processRepository.update(processLog);
                });
    }

    @Override
    public void assignDeviceValidation(final ProcessId processId, final DeviceValidation deviceValidation) {
        this.processRepository.findById(processId)
                .ifPresent(processLog -> {
                    processLog.applyDeviceValidation(deviceValidation);
                    this.processRepository.update(processLog);
                });
    }

    @Override
    public void assignCommunication(final ProcessId processId, final ProcessCommunication communication) {
        this.processRepository.findById(processId)
                .ifPresent(processLog -> {
                    processLog.applyCommunication(communication);
                    this.processRepository.update(processLog);
                });
    }

	@Override
	public void updateResponse(final ProcessId processId, final String response) {
		this.processRepository.findById(processId).ifPresent(processLog -> {
			processLog.changeResponse(response);
			this.processRepository.update(processLog);
            DomainContext.eventPublisher().publishEvent(new ProcessResponseUpdated(processLog.snapshot(), Instant.now()));
		});
    }

    @Override
    public void logFinishedProcess(final ProcessId processId) {
		this.processRepository.findById(processId).ifPresent(processLog -> {
			processLog.changeStatus(ProcessStatus.SUCCESS);
			this.processRepository.update(processLog);
            DomainContext.eventPublisher().publishEvent(new ProcessFinished(processLog.snapshot(), Instant.now()));
        });
	}

	@Override
	public void logFailedProcess(final ProcessId processId) {
        logFailedProcess(processId, null);
	}

	@Override
	public void logFailedProcess(final ProcessId processId, final String faultDescription) {
		this.processRepository.findById(processId).ifPresent(processLog -> {
			processLog.changeStatus(ProcessStatus.FAILURE, faultDescription);
			this.processRepository.update(processLog);
            DomainContext.eventPublisher().publishEvent(new ProcessFinished(processLog.snapshot(), Instant.now()));
		});
	}
}
