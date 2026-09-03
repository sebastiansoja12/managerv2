package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorUserServiceAdapterTest {

    private static final OperatorScope OPERATOR_SCOPE = new OperatorScope(OperatorId.of(20L), new UserId(5L));

    private final UserApiService userApiService = mock(UserApiService.class);

    private final OperatorUserServiceAdapter adapter = new OperatorUserServiceAdapter(userApiService,
            new OperatorScopeExecutor(new OperatorContext()));

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReadUsersInsideSelectedOperatorContext() {
        final AtomicReference<OperatorId> operatorDuringCall = new AtomicReference<>();
        when(userApiService.findAll()).thenAnswer(invocation -> {
            operatorDuringCall.set(currentOperatorId());
            return List.of();
        });

        adapter.findAll(OPERATOR_SCOPE);

        assertThat(operatorDuringCall.get()).isEqualTo(OperatorId.of(20L));
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldCreateUserInsideSelectedOperatorContext() {
        final AtomicReference<OperatorId> operatorDuringCall = new AtomicReference<>();
        when(userApiService.createOperatorUser(eq(OperatorId.of(20L)), any())).thenAnswer(invocation -> {
            operatorDuringCall.set(currentOperatorId());
            return new UserIdDto(99L);
        });

        final UserId userId = adapter.create(OPERATOR_SCOPE, new CreateOperatorUserCommand("Jan", "Kowalski",
                "jkowalski", "secret", "jan@test.pl", "USER", new DepartmentCode("KT1"), "pl"));

        assertThat(userId).isEqualTo(new UserId(99L));
        assertThat(operatorDuringCall.get()).isEqualTo(OperatorId.of(20L));
    }

    private OperatorId currentOperatorId() {
        return ((UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getOperatorId();
    }
}
