package com.warehouse.incident.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.incident.application.port.secondary.IncidentActorServicePort;
import com.warehouse.incident.domain.vo.IncidentActor;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;

import java.util.Optional;

public class IncidentActorServiceAdapter implements IncidentActorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    private final UserApiService userApiService;

    private final DepartmentApiService departmentApiService;

    public IncidentActorServiceAdapter(final OperatorContextProvider operatorContextProvider,
                                       final UserApiService userApiService,
                                       final DepartmentApiService departmentApiService) {
        this.operatorContextProvider = operatorContextProvider;
        this.userApiService = userApiService;
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Optional<IncidentActor> currentActor() {
        return this.operatorContextProvider.currentContext()
                .map(OperatorDetails::userId)
                .map(userId -> new IncidentActor(userId,
                        new Username(this.userApiService.findById(userId).username())));
    }

    @Override
    public Optional<DepartmentCode> currentDepartmentCode() {
        return this.operatorContextProvider.currentContext()
                .map(OperatorDetails::departmentId)
                .flatMap(this.departmentApiService::findDepartmentCode);
    }
}
