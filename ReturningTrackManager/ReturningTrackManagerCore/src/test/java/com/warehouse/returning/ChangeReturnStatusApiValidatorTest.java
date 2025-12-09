package com.warehouse.returning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReturnStatusApiRequest;
import com.warehouse.returning.infrastructure.adapter.primary.validator.ChangeReturnStatusApiValidator;

class ChangeReturnStatusApiValidatorTest {

    private final ChangeReturnStatusApiValidator validator = new ChangeReturnStatusApiValidator();

    @Test
    void shouldReturnErrorWhenReturnPackageIdIsMissing() {
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                null,
                "PROCESSING"
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).containsExactly("Return package id must be provided");
    }

    @Test
    void shouldReturnErrorWhenReturnStatusIsMissing() {
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                new ReturnPackageId(123L),
                null
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).containsExactly("Return status must be provided");
    }

    @Test
    void shouldReturnErrorWhenReturnStatusIsInvalid() {
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                new ReturnPackageId(123L),
                "INVALID_STATUS"
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).containsExactly("Invalid return status: INVALID_STATUS");
    }

    @Test
    void shouldReturnSuccessWhenAllFieldsAreValid() {
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                new ReturnPackageId(123L),
                "COMPLETED"
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void shouldReturnResourceNameCorrectly() {
        final String resourceName = validator.getResourceName();
        assertThat(resourceName).isEqualTo("ChangeReturnStatusApiRequest");
    }
}
