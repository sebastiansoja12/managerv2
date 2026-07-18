package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.ChangeReasonCodeRequest;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ReturnTokenValidation;
import com.warehouse.returning.domain.vo.ShipmentId;
import org.junit.jupiter.api.Test;

class ShipmentReturnKafkaListenerTest {

    private final CapturingReturnPort returnPort = new CapturingReturnPort();
    private final ShipmentReturnKafkaListener listener =
            new ShipmentReturnKafkaListener(new ObjectMapper(), this.returnPort);

    @Test
    void shouldProcessShipmentReturnMessage() {
        this.listener.handle("""
                {
                  "shipmentId": 123,
                  "reason": "RETURN",
                  "reasonCode": "NO_LONGER_NEEDED",
                  "departmentCode": "WRO",
                  "userId": 0
                }
                """);

        final ReturnRequest request = this.returnPort.request;
        assertThat(request.getIssuerDepartmentCode().value()).isEqualTo("WRO");
        assertThat(request.getIssuerUserId().value()).isZero();
        assertThat(request.getRequests()).hasSize(1);

        final ReturnPackageRequest packageRequest = request.getRequests().getFirst();
        assertThat(packageRequest.getShipmentId().value()).isEqualTo(123L);
        assertThat(packageRequest.getReason()).isEqualTo("RETURN");
        assertThat(packageRequest.getReasonCode().name()).isEqualTo("NO_LONGER_NEEDED");
        assertThat(packageRequest.getDepartmentCode().value()).isEqualTo("WRO");
        assertThat(packageRequest.getUserId().value()).isZero();
    }

    private static final class CapturingReturnPort implements ReturnPort {

        private ReturnRequest request;

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
        public void complete(final ReturnPackageId returnPackageId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReturnPackage getReturn(final ReturnPackageId returnId) {
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
