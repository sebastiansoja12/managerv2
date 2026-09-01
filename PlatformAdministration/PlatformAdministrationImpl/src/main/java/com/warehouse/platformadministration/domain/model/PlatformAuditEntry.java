package com.warehouse.platformadministration.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.domain.vo.PlatformActor;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record PlatformAuditEntry(Long id,
                                 PlatformOwnerId platformOwnerId,
                                 String platformOwnerUsername,
                                 OperatorId operatorId,
                                 UserId impersonatedUserId,
                                 boolean impersonation,
                                 String httpMethod,
                                 String path,
                                 int statusCode,
                                 boolean success,
                                 Instant createdAt) {

    private static final Pattern OPERATOR_PATH = Pattern.compile("^/operators/(\\d+)(/.*)?$");

    public static PlatformAuditEntry record(final PlatformActor actor,
                                            final String httpMethod,
                                            final String path,
                                            final int statusCode,
                                            final boolean failed) {
        final OperatorId operatorId = actor.impersonation() ? actor.operatorId() : operatorFromPath(path);
        return new PlatformAuditEntry(null, actor.platformOwnerId(), actor.username(), operatorId,
                actor.impersonatedUserId(), actor.impersonation(), httpMethod, path, statusCode,
                !failed && statusCode < 400, Instant.now());
    }

    private static OperatorId operatorFromPath(final String path) {
        final Matcher matcher = OPERATOR_PATH.matcher(path);
        return matcher.matches() ? OperatorId.of(Long.valueOf(matcher.group(1))) : null;
    }
}
