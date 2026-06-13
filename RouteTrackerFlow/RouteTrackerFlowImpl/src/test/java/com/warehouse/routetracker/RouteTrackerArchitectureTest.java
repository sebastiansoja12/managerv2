package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class RouteTrackerArchitectureTest {

    @Test
    void shouldUseLocalShipmentContractWithoutShipmentManagementDependency() throws IOException {
        final String pom = Files.readString(Path.of("pom.xml"));
        final String dockerfile = Files.readString(Path.of("Dockerfile"));
        final Path sourceRoot = Path.of("src/main/java");

        try (Stream<Path> sourceFiles = Files.walk(sourceRoot)) {
            final String sources = sourceFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(this::readSource)
                    .reduce("", String::concat);

            assertThat(pom).doesNotContain("ShipmentManagement");
            assertThat(pom).doesNotContain("<artifactId>Common</artifactId>");
            assertThat(dockerfile).doesNotContain("Common");
            assertThat(sources).doesNotContain("com.warehouse.shipment");
            assertThat(sources).doesNotContain("com.warehouse.commonassets");
        }

        assertThat(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/ShipmentCreatedIntegrationEvent.java"))
                .exists();
        final Path snapshotPackage = Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/snapshot");
        assertThat(snapshotPackage.resolve("ShipmentSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("SenderSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("RecipientSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("MoneySnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("DangerousGoodSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("SignatureSnapshot.java")).exists();

        final String message = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/ShipmentCreatedIntegrationEvent.java"));
        assertThat(message)
                .contains("event.snapshot.ShipmentSnapshot")
                .contains("ShipmentSnapshot payload")
                .contains("extends ShipmentChangedIntegrationEvent")
                .doesNotContain("UUID eventId")
                .doesNotContain("Instant occurredAt")
                .doesNotContain("String eventType")
                .doesNotContain("int version")
                .doesNotContain("com.warehouse.commonassets");
        final String changedMessage = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/ShipmentChangedIntegrationEvent.java"));
        assertThat(changedMessage)
                .contains("extends OperatorAwareContext")
                .contains("ShipmentSnapshot payload")
                .doesNotContain("UUID eventId")
                .doesNotContain("Instant occurredAt")
                .doesNotContain("private final String eventType")
                .doesNotContain("int version");
        assertThat(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/OperatorAwareContext.java"))
                .exists();

        final String listener = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/ShipmentKafkaListener.java"));
        assertThat(listener)
                .contains("handle(final ShipmentCreatedIntegrationEvent message)")
                .contains("shipmentKafkaEventMapper.map(message)")
                .doesNotContain("@Header")
                .doesNotContain("ConsumerRecord")
                .doesNotContain("ObjectMapper")
                .doesNotContain("fromIntegrationEvent")
                .doesNotContain("new ShipmentStatusStateChangeCommand")
                .doesNotContain("com.warehouse.commonassets");

        try (Stream<Path> eventSources = Files.walk(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event"))) {
            final String localContracts = eventSources
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(this::readSource)
                    .reduce("", String::concat);
            assertThat(localContracts).doesNotContain("com.warehouse.commonassets");
        }
    }

    @Test
    void kafkaConfigurationShouldNotDependOnProducerJavaTypeHeaders() throws IOException {
        final String configuration = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/configuration/ShipmentKafkaConfiguration.java"));

        assertThat(configuration)
                .doesNotContain("DefaultJackson2JavaTypeMapper")
                .doesNotContain("idClassMapping")
                .doesNotContain("__TypeId__")
                .doesNotContain("TYPE_ID");
    }

    @Test
    void routeLogDetailEntityShouldUsePersistenceSpecificIdentifiers() throws IOException {
        final String entity = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/secondary/entity/RouteLogRecordDetailEntity.java"));

        assertThat(entity)
                .contains("private OperatorId operatorId")
                .contains("private UserId userId")
                .contains("private DepartmentId departmentId")
                .contains("private SupplierId supplierId")
                .doesNotContain("private Long operatorId")
                .doesNotContain("private Long userId")
                .doesNotContain("private Long departmentId")
                .doesNotContain("private Long supplierId");
    }

    @Test
    void routeLogRecordEntityShouldUsePersistenceSpecificIdentifier() throws IOException {
        final String entity = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/secondary/entity/RouteLogRecordEntity.java"));

        assertThat(entity)
                .contains("private RouteLogRecordId id")
                .doesNotContain("private String id");
    }

    @Test
    void routeLogToEntityMapperShouldNotUseSetters() throws IOException {
        final String mapper = Files.readString(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/secondary/mapper/RouteLogToEntityMapper.java"));

        assertThat(mapper)
                .contains("RouteLogRecordEntity.builder()")
                .contains("RouteLogRecordDetailEntity.builder()")
                .doesNotContain(".set")
                .doesNotContain("mapWithoutDetails");
    }

    private String readSource(final Path path) {
        try {
            return Files.readString(path);
        } catch (final IOException exception) {
            throw new IllegalStateException("Cannot read source file: " + path, exception);
        }
    }
}
