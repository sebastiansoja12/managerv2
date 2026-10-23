package com.warehouse.e2e;

import static com.warehouse.e2e.LogisticsE2EFixture.DELIVERY_REJECT_SEED;
import static com.warehouse.e2e.LogisticsE2EFixture.DEPARTMENT_CODE;
import static com.warehouse.e2e.LogisticsE2EFixture.DEPARTMENT_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.REJECTED_DELIVERY_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.REJECTED_SHIPMENT_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.REJECTED_SHIPMENT_TRACKING_NUMBER;
import static com.warehouse.e2e.LogisticsE2EFixture.REJECT_REASON;
import static com.warehouse.e2e.LogisticsE2EFixture.SUPPLIER_CODE;
import static com.warehouse.e2e.LogisticsE2EFixture.TERMINAL_DEVICE_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.TERMINAL_DEVICE_SEED;
import static com.warehouse.e2e.LogisticsE2EFixture.TERMINAL_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.warehouse.commonassets.identificator.ShipmentId;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeliveryRejectE2ETest extends AbstractE2ETest {

    private static final Duration ASYNC_TIMEOUT = Duration.ofSeconds(30);

    private TerminalSoapClient.SoapResponse response;

    private UUID processId;

    DeliveryRejectE2ETest(@LocalServerPort final int serverPort, final DataSource dataSource) {
        super(serverPort, dataSource);
    }

    @BeforeAll
    void rejectShipmentFromTerminal() {
        executeSqlScript(TERMINAL_DEVICE_SEED);
        executeSqlScript(DELIVERY_REJECT_SEED);

        response = terminalSoapClient.sendTerminalRequest(LogisticsE2EFixture.deliveryRejectRequest());

        assertThat(response.statusCode())
                .as("SOAP response: %s", response.body())
                .isEqualTo(200);
        processId = UUID.fromString(response.text("TerminalResponse/ProcessId"));
    }

    @Test
    void shouldRespondWithRejectedShipmentDetails() {
        final String detail = "DeliveryRejectResponse/DeliveryRejectResponseDetails/DeliveryRejectResponseDetail";

        assertThat(response.count(detail)).isEqualTo(1);
        assertThat(response.text(detail + "/TrackingNumber")).isEqualTo(REJECTED_SHIPMENT_TRACKING_NUMBER.value());
        assertThat(response.count(detail + "/ShipmentID")).isZero();
        assertThat(response.text(detail + "/DeliveryStatus")).isEqualTo("REJECTED");
        assertThat(response.text(detail + "/RejectReason")).isEqualTo(REJECT_REASON);
        assertThat(response.text(detail + "/DepartmentCode")).isEqualTo(DEPARTMENT_CODE.value());
        assertThat(response.text(detail + "/SupplierCode")).isEqualTo(SUPPLIER_CODE.value());
    }

    @Test
    void shouldRespondWithNewTrackingNumberOfRedirectedShipment() {
        final ShipmentId redirectedShipmentId = redirectedShipmentId();

        final String newTrackingNumber = response.text(
                "DeliveryRejectResponse/DeliveryRejectResponseDetails/DeliveryRejectResponseDetail/NewTrackingNumber");

        assertThat(newTrackingNumber).isEqualTo(shipment(redirectedShipmentId).get("tracking_number"));
    }

    @Test
    void shouldFinishProcessSuccessfullyWithRequestAndResponseStoredInProcessHub() {
        final Map<String, Object> processLog = jdbcClient.sql("""
                        SELECT status, request, response, fault_description, device_id, department_code, user_id
                        FROM process_logs
                        WHERE process_id = :processId
                        """)
                .param("processId", processId)
                .query()
                .singleRow();

        assertThat(processLog.get("status")).isEqualTo("SUCCESS");
        assertThat(processLog.get("fault_description")).isNull();
        assertThat(processLog.get("device_id")).isEqualTo(TERMINAL_DEVICE_ID.value());
        assertThat(processLog.get("department_code")).isEqualTo(DEPARTMENT_CODE.value());
        assertThat(((Number) processLog.get("user_id")).longValue()).isEqualTo(TERMINAL_USER_ID.value());
        assertThat((String) processLog.get("request"))
                .contains(REJECTED_SHIPMENT_TRACKING_NUMBER.value())
                .contains(REJECT_REASON);
        assertThat((String) processLog.get("response")).contains(processId.toString());
    }

    @Test
    void shouldLogShipmentRejectionCommunicationInProcessHub() {
        final Map<String, Object> communication = jdbcClient.sql("""
                        SELECT source_service, target_service, request, response, fault_description
                        FROM communication_log_details
                        WHERE process_id = :processId
                          AND service_type = 'DELIVERY_OPERATION'
                          AND process_type = 'REJECT'
                        """)
                .param("processId", processId)
                .query()
                .singleRow();

        assertThat(communication.get("source_service")).isEqualTo("DELIVERY_OPERATION");
        assertThat(communication.get("target_service")).isEqualTo("SHIPMENT_REJECTION");
        assertThat(communication.get("fault_description")).isNull();
        assertThat((String) communication.get("request")).contains(String.valueOf(REJECTED_SHIPMENT_ID.getValue()));
        assertThat((String) communication.get("response")).isNotBlank();
    }

    @Test
    void shouldRegisterRejectReasonWithDepartmentOfAuthenticatedDevice() {
        final Map<String, Object> rejectReason = jdbcClient.sql("""
                        SELECT reason, reject_department_code, supplier_code, delivery_status
                        FROM reject_reason
                        WHERE shipment_id = :shipmentId
                        """)
                .param("shipmentId", REJECTED_SHIPMENT_ID.getValue())
                .query()
                .singleRow();

        assertThat(rejectReason.get("reason")).isEqualTo(REJECT_REASON);
        assertThat(rejectReason.get("reject_department_code")).isEqualTo(DEPARTMENT_CODE.value());
        assertThat(rejectReason.get("supplier_code")).isEqualTo(SUPPLIER_CODE.value());
        assertThat(rejectReason.get("delivery_status")).isEqualTo("REJECTED");
    }

    @Test
    void shouldRedirectRejectedShipmentAndCreateShipmentBackToSender() {
        final ShipmentId redirectedShipmentId = redirectedShipmentId();

        final Map<String, Object> rejectedShipment = shipment(REJECTED_SHIPMENT_ID);
        final Map<String, Object> redirectedShipment = shipment(redirectedShipmentId);

        assertThat(rejectedShipment.get("status")).isEqualTo("REDIRECT");
        assertThat(rejectedShipment.get("type")).isEqualTo("CHILD");
        assertThat(rejectedShipment.get("locked")).isEqualTo(true);
        assertThat(redirectedShipment.get("type")).isEqualTo("PARENT");
        assertThat(redirectedShipment.get("first_name")).isEqualTo(rejectedShipment.get("recipient_first_name"));
        assertThat(redirectedShipment.get("sender_city")).isEqualTo(rejectedShipment.get("recipient_city"));
        assertThat(redirectedShipment.get("recipient_first_name")).isEqualTo(rejectedShipment.get("first_name"));
        assertThat(redirectedShipment.get("recipient_city")).isEqualTo(rejectedShipment.get("sender_city"));
        assertThat(redirectedShipment.get("tracking_number")).isNotEqualTo(rejectedShipment.get("tracking_number"));
    }

    @Test
    void shouldRecordFailedDeliveryAttemptByAuthenticatedTerminalUser() {
        final List<Map<String, Object>> attempts = jdbcClient.sql("""
                        SELECT outcome, delivery_status, failure_reason, department_id, user_id
                        FROM delivery_step
                        WHERE delivery_id = :deliveryId
                          AND outcome = 'FAILED'
                        """)
                .param("deliveryId", REJECTED_DELIVERY_ID)
                .query()
                .listOfRows();

        assertThat(attempts).hasSize(1);
        final Map<String, Object> attempt = attempts.getFirst();
        assertThat(attempt.get("delivery_status")).isEqualTo("REJECTED");
        assertThat(attempt.get("failure_reason")).isEqualTo(REJECT_REASON);
        assertThat(((Number) attempt.get("department_id")).longValue()).isEqualTo(DEPARTMENT_ID.value());
        assertThat(((Number) attempt.get("user_id")).longValue()).isEqualTo(TERMINAL_USER_ID.value());
    }

    @Test
    void shouldPublishDeliveryAttemptRecordedEventToKafka() {
        await().atMost(ASYNC_TIMEOUT).untilAsserted(() -> {
            final List<Map<String, Object>> events = jdbcClient.sql("""
                            SELECT topic, published_at, last_error
                            FROM kafka_event_outbox
                            WHERE event_type = 'delivery.attempt-recorded'
                              AND payload_json LIKE :deliveryId
                            """)
                    .param("deliveryId", "%" + REJECTED_DELIVERY_ID + "%")
                    .query()
                    .listOfRows();

            assertThat(events).hasSize(1);
            assertThat(events.getFirst().get("topic")).isEqualTo("delivery.events");
            assertThat(events.getFirst().get("last_error")).isNull();
            assertThat(events.getFirst().get("published_at")).isNotNull();
        });
    }

    @Test
    void shouldCreateDerivedDeliveryForRedirectedShipmentThroughKafka() {
        final ShipmentId redirectedShipmentId = redirectedShipmentId();

        await().atMost(ASYNC_TIMEOUT).untilAsserted(() -> {
            final List<Map<String, Object>> deliveries = jdbcClient.sql("""
                            SELECT delivery_type, status
                            FROM delivery
                            WHERE target_type = 'SHIPMENT'
                              AND shipment_id = :shipmentId
                            """)
                    .param("shipmentId", redirectedShipmentId.getValue())
                    .query()
                    .listOfRows();

            assertThat(deliveries).hasSize(1);
            assertThat(deliveries.getFirst().get("status")).isEqualTo("CREATED");
        });
    }

    private ShipmentId redirectedShipmentId() {
        return jdbcClient.sql("SELECT shipment_related_id FROM shipment WHERE shipment_id = :shipmentId")
                .param("shipmentId", REJECTED_SHIPMENT_ID.getValue())
                .query(Long.class)
                .optional()
                .map(ShipmentId::new)
                .orElseThrow(() -> new AssertionError("Rejected shipment has no related shipment, shipment rejection response: "
                        + shipmentRejectionResponse()));
    }

    private String shipmentRejectionResponse() {
        return jdbcClient.sql("""
                        SELECT response
                        FROM communication_log_details
                        WHERE process_id = :processId
                          AND service_type = 'DELIVERY_OPERATION'
                        """)
                .param("processId", processId)
                .query(String.class)
                .optional()
                .orElse("<none>");
    }

    private Map<String, Object> shipment(final ShipmentId shipmentId) {
        return jdbcClient.sql("""
                        SELECT status, type, locked, tracking_number, first_name, sender_city,
                               recipient_first_name, recipient_city
                        FROM shipment
                        WHERE shipment_id = :shipmentId
                        """)
                .param("shipmentId", shipmentId.getValue())
                .query()
                .singleRow();
    }
}
