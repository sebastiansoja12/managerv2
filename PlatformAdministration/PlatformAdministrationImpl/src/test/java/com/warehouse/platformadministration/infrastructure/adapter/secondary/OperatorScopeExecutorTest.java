package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class OperatorScopeExecutorTest {

    private final OperatorScopeExecutor operatorScopeExecutor = new OperatorScopeExecutor(new OperatorContext());

    @Test
    void shouldRunOperationInSelectedOperatorContext() {
        final OperatorScope operatorScope = new OperatorScope(OperatorId.of(10L), new UserId(5L));

        final UsernameTenantPasswordAuthenticationToken authentication = operatorScopeExecutor.execute(operatorScope,
                () -> (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());

        assertThat(authentication.getOperatorId()).isEqualTo(OperatorId.of(10L));
        assertThat(authentication.getPrincipal()).isEqualTo(new UserId(5L));
    }
}
