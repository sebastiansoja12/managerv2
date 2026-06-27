package com.warehouse.process.domain.port.secondary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;

import java.util.Optional;

public interface ProcessRepository {
    void create(final ProcessLog processLog);
    void update(final ProcessLog processLog);
    Optional<ProcessLog> findById(final ProcessId processId);
    Optional<ProcessLog> findByIdForCurrentDepartment(final ProcessId processId);
    Page<ProcessLog> findAllForCurrentDepartment(final Pageable pageable);
}
