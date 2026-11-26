package com.warehouse.platformadministration.application.service;

import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OperatorJobRegistry {

    private final Map<OperatorJobType, OperatorJobServicePort> jobs = new EnumMap<>(OperatorJobType.class);

    public OperatorJobRegistry(final List<OperatorJobServicePort> jobServices) {
        jobServices.forEach(job -> this.jobs.put(job.jobType(), job));
    }

    public List<OperatorJobType> jobTypes() {
        return List.copyOf(this.jobs.keySet());
    }

    public Optional<OperatorJobServicePort> find(final OperatorJobType jobType) {
        return Optional.ofNullable(this.jobs.get(jobType));
    }
}
