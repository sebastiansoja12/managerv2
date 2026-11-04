package com.warehouse.organisationstructure.department.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentLifecycleParticipant;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentLifecycleServicePort;

import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.Optional;

public class DepartmentLifecycleServiceAdapter implements DepartmentLifecycleServicePort {

    private final ObjectProvider<DepartmentLifecycleParticipant> participants;

    public DepartmentLifecycleServiceAdapter(final ObjectProvider<DepartmentLifecycleParticipant> participants) {
        this.participants = participants;
    }

    @Override
    public void notifyCreated(final DepartmentId departmentId) {
        this.participants.orderedStream().forEach(participant -> participant.departmentCreated(departmentId));
    }

    @Override
    public List<String> activationBlockers(final DepartmentId departmentId) {
        return this.participants.orderedStream()
                .map(participant -> participant.activationBlocker(departmentId))
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public List<String> activeWork(final DepartmentId departmentId) {
        return this.participants.orderedStream()
                .map(participant -> participant.activeWork(departmentId))
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public void transferWork(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
        this.participants.orderedStream()
                .forEach(participant -> participant.transferWork(sourceDepartmentId, targetDepartmentId));
    }
}
