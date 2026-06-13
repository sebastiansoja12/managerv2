package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.*;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.OperatorId;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCanceled;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentReturnCreated;
import com.warehouse.returning.infrastructure.adapter.primary.kafka.event.ShipmentSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentReturnKafkaListenerTest {

    private final CapturingReturnPort returnPort = new CapturingReturnPort();
    private final ShipmentReturnKafkaListener listener = new ShipmentReturnKafkaListener(this.returnPort);

    @Test
    void shouldProcessShipmentReturnCreatedEvent() {
        this.listener.handle(new ShipmentReturnCreated(
                new ShipmentSnapshot(new ShipmentId(123L), "RETURN"),
                Instant.parse("2026-08-23T08:00:00Z"),
                "NO_LONGER_NEEDED",
                "RETURN",
                new DepartmentCode("WRO"),
                new UserId(0L),
                new OperatorId(77L)));

        final ReturnRequest request = this.returnPort.request;
        assertThat(request.getIssuerDepartmentCode().value()).isEqualTo("WRO");
        assertThat(request.getIssuerUserId().value()).isZero();
        assertThat(request.getOperatorId()).isEqualTo(77L);
        assertThat(request.getRequests()).hasSize(1);

        final ReturnPackageRequest packageRequest = request.getRequests().getFirst();
        assertThat(packageRequest.getShipmentId().value()).isEqualTo(123L);
        assertThat(packageRequest.getReason()).isEqualTo("RETURN");
        assertThat(packageRequest.getReasonCode().name()).isEqualTo("NO_LONGER_NEEDED");
        assertThat(packageRequest.getDepartmentCode().value()).isEqualTo("WRO");
        assertThat(packageRequest.getUserId().value()).isZero();
    }

    @Test
    void shouldCancelReturnWhenShipmentReturnCanceledEventArrives() {
        this.listener.handle(new ShipmentReturnCanceled(
                new ShipmentSnapshot(new ShipmentId(123L), "DELIVERY"),
                Instant.parse("2026-08-23T08:00:00Z"),
                new UserId(0L),
                new OperatorId(77L),
                new ShipmentId(123L)));

        assertThat(this.returnPort.canceledShipmentId.value()).isEqualTo(123L);
    }

    private static final class CapturingReturnPort implements ReturnPort {

        private ReturnRequest request;
        private ShipmentId canceledShipmentId;

        @Override
        public ReturnResponse process(final ReturnRequest request) {
            this.request = request;
            return null;
        }

        @Override
        public void changeReasonCode(final ChangeReasonCodeRequest request) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void complete(final ShipmentId shipmentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void cancel(final ShipmentId shipmentId) {
            this.canceledShipmentId = shipmentId;
        }

        @Override
        public ReturnPackage getReturn(final ReturnPackageId returnId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReturnPage getReturns(
                final DepartmentCode departmentCode,
                final Long operatorId,
                final int page,
                final int size) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReturnTokenValidation validateReturnToken(final ShipmentId shipmentId, final ReturnToken returnToken) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete(final ReturnPackageId returnPackageId) {
            throw new UnsupportedOperationException();
        }
    }
}
