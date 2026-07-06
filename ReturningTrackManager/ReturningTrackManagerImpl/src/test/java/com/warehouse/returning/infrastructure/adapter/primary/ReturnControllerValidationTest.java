package com.warehouse.returning.infrastructure.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Set;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.service.ApiKeyService;
import com.warehouse.returning.domain.vo.DecodedApiOperator;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnPackageApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnStatusApi;
import org.springframework.http.ResponseEntity;

import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

class ReturnControllerValidationTest {

    @Test
    void shouldReadLatestReturnForShipmentAndAuthenticatedOperator() {
        final ReturnPort returnPort = mock(ReturnPort.class);
        final ApiKeyService apiKeyService = mock(ApiKeyService.class);
        final ReturnController controller = new ReturnController(returnPort, Set.of(), apiKeyService);
        final ReturnPackage returnPackage = ReturnLookupFixture.cancelledReturn();
        when(apiKeyService.decodeJwt(null)).thenReturn(
                new DecodedApiOperator(new UserId(1L), new DepartmentCode("KT1"), 7L, "operator"));
        when(returnPort.findLatestReturn(returnPackage.getShipmentId(), 7L)).thenReturn(Optional.of(returnPackage));

        final ResponseEntity<ReturnPackageApi> response = controller.getByShipmentId(returnPackage.getShipmentId().value());

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().returnPackageId().value()).isEqualTo(9223372036854775001L);
        assertThat(response.getBody().returnStatus()).isEqualTo(ReturnStatusApi.CANCELLED);
        verify(returnPort).findLatestReturn(new ShipmentId(6805406359141427429L), 7L);
    }

    @Test
    void shouldReturnNoContentWhenShipmentHasNoReturn() {
        final ReturnPort returnPort = mock(ReturnPort.class);
        final ApiKeyService apiKeyService = mock(ApiKeyService.class);
        final ReturnController controller = new ReturnController(returnPort, Set.of(), apiKeyService);
        when(apiKeyService.decodeJwt(null)).thenReturn(
                new DecodedApiOperator(new UserId(1L), new DepartmentCode("KT1"), 7L, "operator"));
        when(returnPort.findLatestReturn(new ShipmentId(42L), 7L)).thenReturn(Optional.empty());

        final ResponseEntity<ReturnPackageApi> response = controller.getByShipmentId(42L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldRejectLookupWithoutOperatorContext() {
        final ReturnPort returnPort = mock(ReturnPort.class);
        final ApiKeyService apiKeyService = mock(ApiKeyService.class);
        final ReturnController controller = new ReturnController(returnPort, Set.of(), apiKeyService);
        when(apiKeyService.decodeJwt(null)).thenReturn(
                new DecodedApiOperator(new UserId(1L), new DepartmentCode("KT1"), null, "operator"));

        assertThatThrownBy(() -> controller.getByShipmentId(42L))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Operator ID is required");
        verifyNoInteractions(returnPort);
    }

    @Test
    void shouldDeclareReturnsQueryValidationAtTheControllerBoundary() throws NoSuchMethodException {
        final Method getAll = ReturnController.class.getMethod(
                "getAll", String.class, int.class, int.class);
        final Parameter[] parameters = getAll.getParameters();

        assertThat(parameters[0].getAnnotation(NotBlank.class)).isNotNull();
        assertThat(parameters[1].getAnnotation(Min.class).value()).isZero();
        assertThat(parameters[2].getAnnotation(Min.class).value()).isEqualTo(1L);
        assertThat(parameters[2].getAnnotation(Max.class).value()).isEqualTo(100L);
    }
}
