package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.warehouse.auth.domain.model.CreateOperatorUserCommand;
import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.commonassets.identificator.OperatorId;

class OperatorUserRequestMapperTest {

    @Test
    void shouldMapOperatorUserRequestToCommand() {
        final CreateOperatorUserApiRequest request = new CreateOperatorUserApiRequest(
                "Sebastian",
                "Soja",
                "s-soja",
                "secret",
                "sebastian@test.pl",
                "ADMIN",
                "TST",
                "PL"
        );

        final CreateOperatorUserCommand command = OperatorUserRequestMapper.toCommand(OperatorId.of(12L), request);

        assertThat(command.operatorId()).isEqualTo(OperatorId.of(12L));
        assertThat(command.firstName()).isEqualTo("Sebastian");
        assertThat(command.lastName()).isEqualTo("Soja");
        assertThat(command.username()).isEqualTo("s-soja");
        assertThat(command.password()).isEqualTo("secret");
        assertThat(command.email()).isEqualTo("sebastian@test.pl");
        assertThat(command.role()).isEqualTo("ADMIN");
        assertThat(command.departmentCode().getValue()).isEqualTo("TST");
        assertThat(command.language()).isEqualTo("PL");
    }
}
