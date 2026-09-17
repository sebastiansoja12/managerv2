package com.warehouse.auditlog.application.port.secondary;

import com.warehouse.auditlog.domain.vo.AuditLogActor;

import java.util.Optional;

public interface AuditLogActorServicePort {

    Optional<AuditLogActor> currentActor();
}
