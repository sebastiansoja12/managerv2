package com.warehouse.auditlog.infrastructure.adapter.secondary;

import com.warehouse.auditlog.application.port.secondary.AuditLogActorServicePort;
import com.warehouse.auditlog.domain.vo.AuditLogActor;
import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;

import java.util.Optional;

public class AuditLogActorServiceAdapter implements AuditLogActorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    private final UserApiService userApiService;

    public AuditLogActorServiceAdapter(final OperatorContextProvider operatorContextProvider,
                                       final UserApiService userApiService) {
        this.operatorContextProvider = operatorContextProvider;
        this.userApiService = userApiService;
    }

    @Override
    public Optional<AuditLogActor> currentActor() {
        return this.operatorContextProvider.currentContext()
                .filter(context -> context.operatorId() != null)
                .map(this::toActor);
    }

    private AuditLogActor toActor(final OperatorDetails context) {
        final UserId userId = context.userId();
        return new AuditLogActor(context.operatorId(), context.departmentId(), userId,
                userId == null ? null : new Username(this.userApiService.findById(userId).username()));
    }
}
