package com.warehouse.platformadministration.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.exception.OperatorDataErasureNotAllowedException;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperatorDataErasureTest {

    @Test
    void shouldAllowErasureOfBlockedOperatorWithExactCompanyName() {
        assertThatCode(() -> OperatorDataErasure.verify(profile("INACTIVE"), "  Acme Sp. z o.o. "))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldRequireBlockedOperator() {
        assertThrows(OperatorDataErasureNotAllowedException.class,
                () -> OperatorDataErasure.verify(profile("SUSPENDED"), "Acme Sp. z o.o."));
    }

    @Test
    void shouldRequireCaseSensitiveConfirmation() {
        assertThrows(OperatorDataErasureNotAllowedException.class,
                () -> OperatorDataErasure.verify(profile("INACTIVE"), "acme sp. z o.o."));
        assertThrows(OperatorDataErasureNotAllowedException.class,
                () -> OperatorDataErasure.verify(profile("INACTIVE"), null));
    }

    private OperatorProfile profile(final String status) {
        return new OperatorProfile(OperatorId.of(20L), "Acme Sp. z o.o.", "1", null, null, null, null, null, status, null);
    }
}
