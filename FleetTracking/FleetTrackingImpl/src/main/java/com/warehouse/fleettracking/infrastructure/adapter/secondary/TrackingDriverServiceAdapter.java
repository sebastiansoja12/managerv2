package com.warehouse.fleettracking.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.fleettracking.application.port.secondary.TrackingDriverServicePort;
import com.warehouse.fleettracking.domain.vo.TrackingDriver;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;

import java.util.Optional;

public class TrackingDriverServiceAdapter implements TrackingDriverServicePort {

    private final OperatorContextProvider operatorContextProvider;

    private final UserApiService userApiService;

    private final DepartmentApiService departmentApiService;

    public TrackingDriverServiceAdapter(final OperatorContextProvider operatorContextProvider,
                                       final UserApiService userApiService,
                                       final DepartmentApiService departmentApiService) {
        this.operatorContextProvider = operatorContextProvider;
        this.userApiService = userApiService;
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Optional<TrackingDriver> currentDriver() {
        return this.operatorContextProvider.currentContext()
                .map(OperatorDetails::userId)
                .map(userId -> new TrackingDriver(userId,
                        new Username(this.userApiService.findById(userId).username())));
    }

    @Override
    public Optional<DepartmentCode> currentDepartmentCode() {
        return this.operatorContextProvider.currentContext()
                .map(OperatorDetails::departmentId)
                .flatMap(this.departmentApiService::findDepartmentCode);
    }
}
