package com.warehouse.returning.application.port.secondary;

import java.util.UUID;
import com.warehouse.returning.domain.vo.ReturnPackageId;

public interface ReturnConsumedEventServicePort {

    boolean lockAndCheckCancelled(final ReturnPackageId returnPackageId);

    void markCancelled(final ReturnPackageId returnPackageId);

    boolean tryConsume(final UUID eventId);
}
