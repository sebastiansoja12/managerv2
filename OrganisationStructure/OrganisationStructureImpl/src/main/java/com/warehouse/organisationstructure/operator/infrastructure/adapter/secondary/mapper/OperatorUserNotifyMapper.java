package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.event.OperatorCreatedEvent;
import com.warehouse.auth.infrastructure.dto.DepartmentCodeDto;
import com.warehouse.auth.infrastructure.dto.OperatorIdDto;
import com.warehouse.auth.infrastructure.dto.RegisteringUserDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorProvisioningDetails;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

import java.util.function.Consumer;

public final class OperatorUserNotifyMapper {

    private OperatorUserNotifyMapper() {
    }

    public static OperatorCreatedEvent toEvent(final OperatorSnapshot snapshot) {
        return toEvent(snapshot, userId -> {
        });
    }

    public static OperatorCreatedEvent toEvent(final OperatorSnapshot snapshot, final Consumer<UserId> userCreatedId) {
        final OperatorProvisioningDetails provisioningDetails = snapshot.provisioningDetails();
        return new OperatorCreatedEvent(new RegisteringUserDto(
                provisioningDetails.userFirstName(),
                provisioningDetails.userLastName(),
                provisioningDetails.username(),
                provisioningDetails.password(),
                provisioningDetails.language(),
                provisioningDetails.email(),
                new DepartmentCodeDto(provisioningDetails.firstDepartment().departmentCode()),
                new OperatorIdDto(snapshot.operatorId().getValue())
        ), userCreatedId);
    }
}
