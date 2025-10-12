package com.warehouse.returning.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.ReturnPackageIdApi;
import com.warehouse.returning.infrastructure.adapter.primary.validator.ChangeReasonCodeRequestApiValidator;

class ChangeReasonCodeRequestApiValidatorTest {

    private final ChangeReasonCodeRequestApiValidator validator = new ChangeReasonCodeRequestApiValidator();

    @Test
    void shouldReturnErrorWhenReturnPackageIdIsMissing() {
        final ChangeReasonCodeRequestApi request = new ChangeReasonCodeRequestApi(
                null,
                "DAMAGED"
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).containsExactly("Return package id must be provided");
    }

    @Test
    void shouldReturnErrorWhenReasonCodeIsMissing() {
        final ChangeReasonCodeRequestApi request = new ChangeReasonCodeRequestApi(
                new ReturnPackageIdApi(1L),
                null
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).containsExactly("Reason code must be provided");
    }

    @Test
    void shouldReturnErrorWhenReasonCodeIsInvalid() {
        final ChangeReasonCodeRequestApi request = new ChangeReasonCodeRequestApi(
                new ReturnPackageIdApi(1L),
                "INVALID_CODE"
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).containsExactly("Invalid reason code: INVALID_CODE");
    }

    @Test
    void shouldReturnSuccessWhenAllFieldsAreValid() {
        final ChangeReasonCodeRequestApi request = new ChangeReasonCodeRequestApi(
                new ReturnPackageIdApi(1L),
                "DAMAGED"
        );

        final Result<Void, List<String>> result = validator.validateBody(request);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void shouldReturnResourceNameCorrectly() {
        final String resourceName = validator.getResourceName();
        assertThat(resourceName).isEqualTo("ChangeReasonCodeRequestApi");
    }
}

