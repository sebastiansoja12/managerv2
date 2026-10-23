package com.warehouse.process.domain.port.secondary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.model.ProcessLog;

import java.util.Optional;

public interface ProcessRepository {
    void create(final ProcessLog processLog);
    void update(final ProcessLog processLog);
    Optional<ProcessLog> findById(final ProcessId processId);
    Optional<ProcessLog> findByIdForCurrentDepartment(final ProcessId processId);
    Page<ProcessLog> findAllForCurrentDepartment(final Pageable pageable);
    Optional<ProcessLog> findByIdForUser(final ProcessId processId, final UserId userId);
    Page<ProcessLog> findAllForUser(final Pageable pageable, final UserId userId);
}
