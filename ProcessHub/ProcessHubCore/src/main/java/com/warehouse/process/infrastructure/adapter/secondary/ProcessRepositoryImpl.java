package com.warehouse.process.infrastructure.adapter.secondary;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToEntityMapper;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToModelMapper;

public class ProcessRepositoryImpl implements ProcessRepository {

    private final ProcessLogReadRepository readRepository;
    private final ProcessLogWriteRepository writeRepository;

    private final ConcurrentMap<ProcessId, ProcessLog> inFlight = new ConcurrentHashMap<>();
    private final ConcurrentMap<ProcessId, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ProcessRepositoryImpl(final ProcessLogReadRepository readRepository,
                                 final ProcessLogWriteRepository writeRepository) {
        this.readRepository = readRepository;
        this.writeRepository = writeRepository;
    }

    @Override
    public void create(final ProcessLog processLog) {
        final ProcessId processId = processLog.getProcessId();
        final ReentrantLock lock = locks.computeIfAbsent(processId, __ -> new ReentrantLock());

        lock.lock();
        try {
            if (inFlight.containsKey(processId)) {
                throw new RuntimeException("Process log already exists in-memory");
            }
            validateNotExists(processId);
            inFlight.put(processId, processLog);
            final ProcessLogWriteEntity writeEntity = ProcessLogToEntityMapper.map(processLog);
            writeRepository.save(writeEntity);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(final ProcessLog processLog) {
        final ProcessId processId = processLog.getProcessId();
        final ReentrantLock lock = locks.computeIfAbsent(processId, __ -> new ReentrantLock());

        lock.lock();
        try {
            if (!inFlight.containsKey(processId)) {
                throw new RuntimeException(
                        "Process log not found in-memory for update: " + processId);
            }
            inFlight.put(processId, processLog);
        } finally {
            lock.unlock();
            if (processLog.successed()) {
                finish(processId);
            } else if (processLog.failed()) {
                fail(processId);
            }
        }
    }

    @Override
    public Optional<ProcessLog> findById(final ProcessId processId) {
        final Optional<ProcessLog> inMemory = Optional.ofNullable(inFlight.get(processId));
        if (inMemory.isPresent()) {
            return inMemory;
        }

        return readRepository.findById(processId)
                .map(ProcessLogToModelMapper::map);
    }

    public void finish(final ProcessId processId) {
        flushAndRemove(processId);
    }

    public void fail(final ProcessId processId) {
        flushAndRemove(processId);
    }

    private void flushAndRemove(final ProcessId processId) {
        final ReentrantLock lock = locks.computeIfAbsent(processId, __ -> new ReentrantLock());

        lock.lock();
        try {
            final ProcessLog current = inFlight.remove(processId);
            if (current != null) {
                final ProcessLogWriteEntity writeEntity = ProcessLogToEntityMapper.map(current);
                writeRepository.save(writeEntity);
            }
        } finally {
            lock.unlock();
            if (!lock.isLocked()) {
                locks.remove(processId, lock);
            }
        }
    }

    private void validateNotExists(final ProcessId processId) {
        final Optional<ProcessLogReadEntity> existing =
                readRepository.findById(processId);

        if (existing.isPresent()) {
            throw new RuntimeException("Process log record already exists");
        }
    }
}
