package com.warehouse.incident.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.incident.domain.vo.IncidentActor;

import java.util.Optional;

public interface IncidentActorServicePort {

    Optional<IncidentActor> currentActor();

    Optional<DepartmentCode> currentDepartmentCode();
}
