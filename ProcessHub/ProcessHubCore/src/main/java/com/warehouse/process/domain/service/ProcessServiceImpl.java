package com.warehouse.process.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.exception.ProcessLogNotFoundException;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessRepository;

public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;

    public ProcessServiceImpl(final ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Override
    public ProcessId nextProcessId() {
        return new ProcessId(UUID.randomUUID());
    }

    @Override
    public void createProcess(final ProcessLog processLog) {
        this.processRepository.create(processLog);
    }

    @Override
    public ProcessLog findById(final ProcessId processId) {
        return this.processRepository.findById(processId)
                .orElseThrow(() -> new ProcessLogNotFoundException("Process log not found"));
    }

    @Override
    public void updateResponse(final ProcessId processId, final String response) {
		this.processRepository.findById(processId).ifPresent(processLog -> {
			processLog.changeResponse(response);
			this.processRepository.update(processLog);
		});
    }

    @Override
    public void logFinishedProcess(final ProcessId processId) {
		this.processRepository.findById(processId).ifPresent(processLog -> {
			processLog.changeStatus(ProcessStatus.SUCCESS);
			this.processRepository.update(processLog);
		});
	}

	@Override
	public void logFailedProcess(final ProcessId processId) {
		this.processRepository.findById(processId).ifPresent(processLog -> {
			processLog.changeStatus(ProcessStatus.FAILURE);
			this.processRepository.update(processLog);
		});
	}
}
