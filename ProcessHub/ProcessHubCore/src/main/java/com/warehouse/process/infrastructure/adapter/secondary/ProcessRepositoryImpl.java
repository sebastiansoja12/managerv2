package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.process.domain.port.secondary.ProcessRepository;

public class ProcessRepositoryImpl implements ProcessRepository {

    private final ProcessLogReadRepository readRepository;
    private final ProcessLogWriteRepository writeRepository;

    public ProcessRepositoryImpl(final ProcessLogReadRepository readRepository,
                                 final ProcessLogWriteRepository writeRepository) {
        this.readRepository = readRepository;
        this.writeRepository = writeRepository;
    }
}
