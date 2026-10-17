package com.warehouse.platformadministration.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformAuditEntryTest {

    @Test
    void shouldResolveOperatorFromPanelPath() {
        final PlatformActor actor = new PlatformActor(new PlatformOwnerId(1L), "owner", false, null, null);

        final PlatformAuditEntry entry = PlatformAuditEntry.record(actor, "POST", "/operators/10/users", 201, false);

        assertThat(entry.operatorId()).isEqualTo(OperatorId.of(10L));
        assertThat(entry.success()).isTrue();
    }

    @Test
    void shouldUseImpersonatedOperatorAndMarkFailure() {
        final PlatformActor actor = new PlatformActor(new PlatformOwnerId(1L), "owner", true, new UserId(5L),
                OperatorId.of(20L));

        final PlatformAuditEntry entry = PlatformAuditEntry.record(actor, "PUT", "/users/7", 403, false);

        assertThat(entry.operatorId()).isEqualTo(OperatorId.of(20L));
        assertThat(entry.impersonatedUserId()).isEqualTo(new UserId(5L));
        assertThat(entry.success()).isFalse();
    }
}
