package com.warehouse.returning;

import com.warehouse.common.DepartmentId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.*;
import com.warehouse.returning.infrastructure.adapter.primary.validator.ReturnRequestApiValidator;

class ReturnRequestApiValidatorTest {

    private final ReturnRequestApiValidator validator = new ReturnRequestApiValidator();

    @Test
    void shouldValidateReturnRequestWhenIsEmpty() {
        final List<ReturnPackageRequestApi> returnPackageRequests = new ArrayList<>();
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[Request must contain at least one package]", result.getFailure().toString());
    }

    @Test
    void shouldValidateReturnRequestWhenShipmentIsEmpty() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(null,
                "Zwrot", new DepartmentId(1L), new UserIdApi(1L), new ReasonCodeApi("DAMAGED"));
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[Shipment id must be provided]", result.getFailure().toString());
    }

    @Test
    void shouldValidateReturnRequestWhenReasonIsEmpty() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(new ShipmentIdApi(1L),
                "", new DepartmentId(1L), new UserIdApi(1L), new ReasonCodeApi("DAMAGED"));
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[Reason must be provided]", result.getFailure().toString());
    }

    @Test
    void shouldValidateReturnRequestWhenDepartmentIdIsEmpty() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(new ShipmentIdApi(1L),
                "Zwrot", null, new UserIdApi(1L), new ReasonCodeApi("DAMAGED"));
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[Department ID must be provided]", result.getFailure().toString());
    }

    @Test
    void shouldValidateReturnRequestWhenUserIdIsEmpty() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(new ShipmentIdApi(1L),
                "Zwrot", new DepartmentId(1L), null, new ReasonCodeApi("DAMAGED"));
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[User id must be provided]", result.getFailure().toString());
    }

    @Test
    void shouldValidateReturnRequestWhenReasonCodeIsNull() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(new ShipmentIdApi(1L),
                "Zwrot", new DepartmentId(1L), new UserIdApi(1L), null);
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[Reason code must be provided]", result.getFailure().toString());
    }

    @Test
    void shouldValidateReturnRequestWhenReasonCodeIsEmpty() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(new ShipmentIdApi(1L),
                "Zwrot", new DepartmentId(1L), new UserIdApi(1L), new ReasonCodeApi(""));
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
        assertEquals("[Reason code must be provided]", result.getFailure().toString());
    }

    @Test
    void shouldValidateAll() {
        final ReturnPackageRequestApi returnPackageRequest = getReturnPackageRequest(new ShipmentIdApi(null),
                "", new DepartmentId(null), new UserIdApi(null), new ReasonCodeApi(""));
        final List<ReturnPackageRequestApi> returnPackageRequests = List.of(returnPackageRequest);
        final ReturnRequestApi request = new ReturnRequestApi(returnPackageRequests);
        final Result result = validator.validateBody(request);
		assertEquals(
				"[Shipment id must be provided, Reason must be provided, Department ID must be provided, User id must be provided, Reason code must be provided]",
				result.getFailure().toString());
    }

    @Test
    void shouldReturnResourceNameCorrectly() {
        final String resourceName = validator.getResourceName();
        assertThat(resourceName).isEqualTo("ReturnRequestApi");
    }

    private ReturnPackageRequestApi getReturnPackageRequest(final ShipmentIdApi shipmentId, final String reason,
                                                            final DepartmentId departmentId,
                                                            final UserIdApi userId, final ReasonCodeApi reasonCode) {
        return new ReturnPackageRequestApi(shipmentId, reason, departmentId, userId, reasonCode);
    }
}
