package com.warehouse.returning.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.application.port.primary.command.ChangeReturnPackageStatusCommand;
import com.warehouse.returning.application.port.primary.command.CreateReturnPackageCommand;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnState;

import java.util.List;

public interface ReturnPort {
    List<CreatedReturn> create(final CreateReturnPackageCommand command);

    void changeStatus(final ChangeReturnPackageStatusCommand command);

    void startProcessing(final ReturnPackageId returnId);

    void complete(final ReturnPackageId returnId);

    void cancel(final ReturnPackageId returnId);

    void changeReasonCode(final ReturnPackageId returnId, final ReasonCode reasonCode);

    boolean validateToken(final ShipmentId shipmentId, final ReturnToken returnToken);

    ReturnState get(final ReturnPackageId returnId);

}
