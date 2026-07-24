package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.returning.application.port.primary.command.ChangeReturnPackageStatusCommand;
import com.warehouse.returning.application.port.primary.command.CreateReturnPackageCommand;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ChangeReturnPackageStatusRequest;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.CreateReturnPackageRequest;
import com.warehouse.returning.domain.enumeration.ReasonCode;

public class ReturnPackageRequestMapper {

    public CreateReturnPackageCommand map(final CreateReturnPackageRequest request, final UserId userId) {
        return new CreateReturnPackageCommand(
                request.shipmentId(),
                request.reason(),
                request.departmentId(),
                userId,
                ReasonCode.valueOf(request.reasonCode())
        );
    }

    public ChangeReturnPackageStatusCommand map(final ReturnPackageId returnPackageId,
                                                final ChangeReturnPackageStatusRequest request) {
        return new ChangeReturnPackageStatusCommand(
                returnPackageId,
                ReturnStatus.valueOf(request.returnStatus()),
                request.processedBy()
        );
    }
}
