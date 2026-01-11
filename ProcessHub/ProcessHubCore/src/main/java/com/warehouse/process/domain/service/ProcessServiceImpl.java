package com.warehouse.process.domain.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessRepository;

public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;
    private final Map<ProcessId, ProcessLog> processLogMap = new HashMap<>();

    public ProcessServiceImpl(final ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Override
    public ProcessId nextProcessId() {
        return new ProcessId(UUID.randomUUID());
    }

    @Override
    public void createProcess(final ProcessLog processLog) {
        if (!processLogMap.containsKey(processLog.getProcessId())) {
            processLogMap.put(processLog.getProcessId(), processLog);
        } else {
            this.processRepository.create(processLog);
        }
    }

    @Override
    public ProcessLog findById(final ProcessId processId) {
        final ProcessLog processLog = processLogMap.get(processId);
        if (processLog == null) {
            return this.processRepository.findById(processId);
        }
        return processLog;
    }

    @Override
    public void updateResponse(final ProcessId processId, final String response) {
        final ProcessLog processLog = this.findById(processId);
        processLog.changeResponse(response);
        this.processRepository.update(processLog);
    }
}
