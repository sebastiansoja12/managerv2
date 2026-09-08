package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.application.port.primary.result.LogisticsResponse;
import com.warehouse.logistics.domain.enumeration.DeliverySaveStatus;
import com.warehouse.logistics.domain.enumeration.UpdateStatus;
import com.warehouse.logistics.domain.vo.DeliveryTarget;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptRequestDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryAttemptResultDto;
import com.warehouse.logisticsdelivery.api.dto.DeliveryTargetDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryServiceAdapterTest {

    @Test
    void shouldRecordReturnAttemptThroughDeliveryApi() {
        final DeliveryApiService deliveryApiService = mock(DeliveryApiService.class);
        final DeliveryServiceAdapter adapter = new DeliveryServiceAdapter(deliveryApiService);
        final DeliveryTargetDto target = new DeliveryTargetDto(DeliveryTargetDto.TargetType.SHIPMENT, "10");
        when(deliveryApiService.recordAttempt(any())).thenReturn(
                new DeliveryAttemptResultDto(new DeliveryId("delivery-1"), new ShipmentId(10L), target));
        final LogisticsRequest request = LogisticsRequest.builder()
                .shipmentId(new ShipmentId(10L))
                .deliveryStatus(DeliveryStatus.RETURN)
                .processType(ProcessType.RETURN)
                .build();

        final LogisticsResponse response = adapter.recordAttempt(request, new UserId(42L), new DepartmentId(7L));

        final ArgumentCaptor<DeliveryAttemptRequestDto> attemptRequest =
                ArgumentCaptor.forClass(DeliveryAttemptRequestDto.class);
        verify(deliveryApiService).recordAttempt(attemptRequest.capture());
        assertThat(attemptRequest.getValue().target()).isEqualTo(target);
        assertThat(attemptRequest.getValue().deliveryType()).isEqualTo(DeliveryAttemptRequestDto.DeliveryType.RETURN);
        assertThat(attemptRequest.getValue().deliveryStatus()).isEqualTo(DeliveryStatus.RETURN);
        assertThat(attemptRequest.getValue().userId()).isEqualTo(new UserId(42L));
        assertThat(attemptRequest.getValue().departmentId()).isEqualTo(new DepartmentId(7L));
        assertThat(response.getDeliveryId()).isEqualTo(new DeliveryId("delivery-1"));
        assertThat(response.getShipmentId()).isEqualTo(new ShipmentId(10L));
        assertThat(response.getUpdateStatus()).isEqualTo(UpdateStatus.OK);
        assertThat(response.getDeliverySaveStatus()).isEqualTo(DeliverySaveStatus.SAVED);
        assertThat(response.getTarget()).isEqualTo(DeliveryTarget.shipment(new ShipmentId(10L)));
    }
}
