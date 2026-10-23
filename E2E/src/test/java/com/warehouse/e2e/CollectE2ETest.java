package com.warehouse.e2e;

import static com.warehouse.e2e.LogisticsE2EFixture.COLLECT_SEED;
import static com.warehouse.e2e.LogisticsE2EFixture.CREATED_SHIPMENT_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.CREATED_SHIPMENT_TRACKING_NUMBER;
import static com.warehouse.e2e.LogisticsE2EFixture.PARTIALLY_COLLECTED_SHIPMENT_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER;
import static com.warehouse.e2e.LogisticsE2EFixture.PREPARED_SHIPMENT_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.PREPARED_SHIPMENT_TRACKING_NUMBER;
import static com.warehouse.e2e.LogisticsE2EFixture.SENT_SHIPMENT_ID;
import static com.warehouse.e2e.LogisticsE2EFixture.SENT_SHIPMENT_TRACKING_NUMBER;
import static com.warehouse.e2e.LogisticsE2EFixture.TERMINAL_DEVICE_SEED;
import static com.warehouse.e2e.LogisticsE2EFixture.UNKNOWN_TRACKING_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.time.LocalDateTime;
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
class CollectE2ETest extends AbstractE2ETest {

    private static final Duration ASYNC_TIMEOUT = Duration.ofSeconds(30);

    private static final String COLLECT_RESPONSE_DETAIL = "CollectResponseDetails/CollectResponseDetail";

    private static final LocalDateTime SEEDED_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 10, 0);

    private TerminalSoapClient.SoapResponse collectedResponse;

    private UUID collectedProcessId;

    private TerminalSoapClient.SoapResponse partiallyCollectedResponse;

    private UUID partiallyCollectedProcessId;

    CollectE2ETest(@LocalServerPort final int serverPort, final DataSource dataSource) {
        super(serverPort, dataSource);
    }

    @BeforeAll
    void collectShipmentsFromTerminal() {
        executeSqlScript(TERMINAL_DEVICE_SEED);
        executeSqlScript(COLLECT_SEED);

        collectedResponse = terminalSoapClient.sendCollectRequest(LogisticsE2EFixture.collectRequest(List.of(
                CREATED_SHIPMENT_TRACKING_NUMBER,
                PREPARED_SHIPMENT_TRACKING_NUMBER)));
        partiallyCollectedResponse = terminalSoapClient.sendCollectRequest(LogisticsE2EFixture.collectRequest(List.of(
                PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER,
                PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER,
                UNKNOWN_TRACKING_NUMBER,
                SENT_SHIPMENT_TRACKING_NUMBER)));

        assertThat(collectedResponse.statusCode())
                .as("SOAP response: %s", collectedResponse.body())
                .isEqualTo(200);
        assertThat(partiallyCollectedResponse.statusCode())
                .as("SOAP response: %s", partiallyCollectedResponse.body())
                .isEqualTo(200);
        collectedProcessId = UUID.fromString(collectedResponse.text("CollectResponse/ProcessId"));
        partiallyCollectedProcessId = UUID.fromString(partiallyCollectedResponse.text("CollectResponse/ProcessId"));
    }

    @Test
    void shouldRespondWithSuccessWhenAllShipmentsAreCollected() {
        final List<Map<String, String>> details = collectedResponse.elements(COLLECT_RESPONSE_DETAIL);

        assertThat(collectedResponse.text("CollectResponse/Success")).isEqualTo("true");
        assertThat(details).containsExactly(
                Map.of("TrackingNumber", CREATED_SHIPMENT_TRACKING_NUMBER.value(),
                        "Success", "true"),
                Map.of("TrackingNumber", PREPARED_SHIPMENT_TRACKING_NUMBER.value(),
                        "Success", "true"));
    }

    @Test
    void shouldRespondWithResultForEachTrackingNumberWhenOnlySomeShipmentsAreCollected() {
        final List<Map<String, String>> details = partiallyCollectedResponse.elements(COLLECT_RESPONSE_DETAIL);

        assertThat(partiallyCollectedResponse.text("CollectResponse/Success")).isEqualTo("false");
        assertThat(details).hasSize(4);
        assertThat(details.get(0)).containsEntry("TrackingNumber", PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER.value())
                .doesNotContainKey("ShipmentID")
                .containsEntry("Success", "true")
                .doesNotContainKey("ErrorMessage");
        assertThat(details.get(1)).containsEntry("TrackingNumber", PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER.value())
                .containsEntry("Success", "false")
                .containsEntry("ErrorMessage", "Tracking number is duplicated in request");
        assertThat(details.get(2)).containsEntry("TrackingNumber", UNKNOWN_TRACKING_NUMBER.value())
                .containsEntry("Success", "false")
                .containsEntry("ErrorMessage", "Shipment was not found");
        assertThat(details.get(3)).containsEntry("TrackingNumber", SENT_SHIPMENT_TRACKING_NUMBER.value())
                .containsEntry("Success", "false")
                .containsEntry("ErrorMessage", "Shipment cannot be collected from status SENT");
    }

    @Test
    void shouldMarkCollectedShipmentsAsSent() {
        assertThat(shipmentStatus(CREATED_SHIPMENT_ID)).isEqualTo("SENT");
        assertThat(shipmentStatus(PREPARED_SHIPMENT_ID)).isEqualTo("SENT");
        assertThat(shipmentStatus(PARTIALLY_COLLECTED_SHIPMENT_ID)).isEqualTo("SENT");
    }

    @Test
    void shouldNotModifyShipmentThatCannotBeCollected() {
        final Map<String, Object> shipment = jdbcClient.sql("""
                        SELECT status, updated_at
                        FROM shipment
                        WHERE shipment_id = :shipmentId
                        """)
                .param("shipmentId", SENT_SHIPMENT_ID.getValue())
                .query()
                .singleRow();

        assertThat(shipment.get("status")).isEqualTo("SENT");
        assertThat(((java.sql.Timestamp) shipment.get("updated_at")).toLocalDateTime()).isEqualTo(SEEDED_UPDATED_AT);
    }

    @Test
    void shouldFinishProcessSuccessfullyWhenAllShipmentsAreCollected() {
        final Map<String, Object> processLog = processLog(collectedProcessId);

        assertThat(processLog.get("status")).isEqualTo("SUCCESS");
        assertThat(processLog.get("fault_description")).isNull();
        assertThat((String) processLog.get("request"))
                .contains(CREATED_SHIPMENT_TRACKING_NUMBER.value())
                .contains(PREPARED_SHIPMENT_TRACKING_NUMBER.value());
        assertThat((String) processLog.get("response")).contains(collectedProcessId.toString());
    }

    @Test
    void shouldFinishProcessSuccessfullyWhenSomeShipmentsAreNotCollected() {
        final Map<String, Object> processLog = processLog(partiallyCollectedProcessId);

        assertThat(processLog.get("status")).isEqualTo("SUCCESS");
        assertThat(processLog.get("fault_description")).isNull();
        assertThat((String) processLog.get("response")).contains(partiallyCollectedProcessId.toString());
    }

    @Test
    void shouldLogShipmentManagementCommunicationForEachCollectedShipment() {
        final List<Map<String, Object>> communications = shipmentManagementCommunications(collectedProcessId);

        assertThat(communications).hasSize(2);
        assertThat(communications).allSatisfy(communication -> {
            assertThat(communication.get("fault_description")).isNull();
            assertThat((String) communication.get("response")).contains("shipmentStatus=SENT");
        });
        assertThat(communications).extracting(communication -> (String) communication.get("request"))
                .anySatisfy(request -> assertThat(request).contains(CREATED_SHIPMENT_TRACKING_NUMBER.value()))
                .anySatisfy(request -> assertThat(request).contains(PREPARED_SHIPMENT_TRACKING_NUMBER.value()));
    }

    @Test
    void shouldLogFaultsOnlyForShipmentsRejectedByShipmentManagement() {
        final List<Map<String, Object>> communications = shipmentManagementCommunications(partiallyCollectedProcessId);

        assertThat(communications).hasSize(3);
        assertThat(communications).filteredOn(communication -> communication.get("fault_description") == null)
                .hasSize(1)
                .first()
                .satisfies(communication -> assertThat((String) communication.get("request"))
                        .contains(PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER.value()));
        assertThat(communications).extracting(communication -> (String) communication.get("fault_description"))
                .contains("Shipment was not found", "Shipment cannot be collected from status SENT");
    }

    @Test
    void shouldPublishShipmentStatusChangedEventForEachCollectedShipment() {
        await().atMost(ASYNC_TIMEOUT).untilAsserted(() -> {
            for (final ShipmentId shipmentId : List.of(CREATED_SHIPMENT_ID, PREPARED_SHIPMENT_ID,
                    PARTIALLY_COLLECTED_SHIPMENT_ID)) {
                final List<Map<String, Object>> events = jdbcClient.sql("""
                                SELECT topic, published_at, last_error
                                FROM kafka_event_outbox
                                WHERE event_type = 'shipment.status.changed'
                                  AND payload_json LIKE :shipmentId
                                """)
                        .param("shipmentId", "%" + shipmentId.getValue() + "%")
                        .query()
                        .listOfRows();

                assertThat(events).as("shipment.status.changed for %s", shipmentId.getValue()).hasSize(1);
                assertThat(events.getFirst().get("topic")).isEqualTo("shipment.events");
                assertThat(events.getFirst().get("last_error")).isNull();
                assertThat(events.getFirst().get("published_at")).isNotNull();
            }
        });
    }

    @Test
    void shouldSyncShipmentReadModelThroughKafka() {
        await().atMost(ASYNC_TIMEOUT).untilAsserted(() -> {
            final List<String> statuses = jdbcClient.sql("""
                            SELECT status
                            FROM shipment_rd
                            WHERE shipment_id IN (:shipmentIds)
                            """)
                    .param("shipmentIds", List.of(CREATED_SHIPMENT_ID.getValue(), PREPARED_SHIPMENT_ID.getValue(),
                            PARTIALLY_COLLECTED_SHIPMENT_ID.getValue()))
                    .query(String.class)
                    .list();

            assertThat(statuses).containsExactly("SENT", "SENT", "SENT");
        });
    }

    private String shipmentStatus(final ShipmentId shipmentId) {
        return jdbcClient.sql("SELECT status FROM shipment WHERE shipment_id = :shipmentId")
                .param("shipmentId", shipmentId.getValue())
                .query(String.class)
                .single();
    }

    private Map<String, Object> processLog(final UUID processId) {
        return jdbcClient.sql("""
                        SELECT status, request, response, fault_description
                        FROM process_logs
                        WHERE process_id = :processId
                        """)
                .param("processId", processId)
                .query()
                .singleRow();
    }

    private List<Map<String, Object>> shipmentManagementCommunications(final UUID processId) {
        return jdbcClient.sql("""
                        SELECT request, response, fault_description
                        FROM communication_log_details
                        WHERE process_id = :processId
                          AND process_type = 'COLLECT'
                          AND source_service = 'DELIVERY_OPERATION'
                          AND target_service = 'SHIPMENT_MANAGEMENT'
                        """)
                .param("processId", processId)
                .query()
                .listOfRows();
    }
}
