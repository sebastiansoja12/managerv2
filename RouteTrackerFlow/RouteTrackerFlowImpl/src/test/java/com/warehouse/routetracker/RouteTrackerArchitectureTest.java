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
        final Path sourceRoot = Path.of("src/main/java");

        try (Stream<Path> sourceFiles = Files.walk(sourceRoot)) {
            final String sources = sourceFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(this::readSource)
                    .reduce("", String::concat);

            assertThat(pom).doesNotContain("ShipmentManagement");
            assertThat(sources).doesNotContain("com.warehouse.shipment");
        }

        assertThat(Path.of(
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/ShipmentEventMessage.java"))
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
                "src/main/java/com/warehouse/routetracker/infrastructure/adapter/primary/kafka/event/ShipmentEventMessage.java"));
        assertThat(message)
                .contains("event.snapshot.ShipmentSnapshot")
                .contains("ShipmentSnapshot payload");
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

    private String readSource(final Path path) {
        try {
            return Files.readString(path);
        } catch (final IOException exception) {
            throw new IllegalStateException("Cannot read source file: " + path, exception);
        }
    }
}
