package com.warehouse.shipment;

import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentArchitectureTest {

    @Test
    void shipmentShouldNotDependOnInfrastructureOrDomainContext() throws IOException {
        final String source = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/domain/model/Shipment.java"));

        assertThat(source).doesNotContain("com.warehouse.shipment.infrastructure");
        assertThat(source).doesNotContain("DomainContext");
        assertThat(source).doesNotContain("DomainRegistry");
    }

    @Test
    void applicationLayerShouldNotDependOnInfrastructure() throws IOException {
        final Path applicationRoot = Path.of("src/main/java/com/warehouse/shipment/application");

        try (java.util.stream.Stream<Path> sourceFiles = Files.walk(applicationRoot)) {
            final List<String> infrastructureImports = sourceFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .flatMap(this::lines)
                    .filter(line -> line.startsWith("import com.warehouse.shipment.infrastructure"))
                    .toList();

            assertThat(infrastructureImports).isEmpty();
        }
    }

    @Test
    void shipmentShouldNotExposeStateSetters() {
        final String[] publicSetters = Arrays.stream(Shipment.class.getDeclaredMethods())
                .filter(method -> java.lang.reflect.Modifier.isPublic(method.getModifiers()))
                .map(Method::getName)
                .filter(name -> name.startsWith("set"))
                .toArray(String[]::new);

        assertThat(publicSetters).isEmpty();
    }

    @Test
    void applicationServiceShouldImplementPrimaryPort() {
        assertThat(ShipmentPort.class).isAssignableFrom(ShipmentPortImpl.class);
    }

    @Test
    void shipmentPortsShouldBelongToApplicationLayer() {
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPort.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPortImpl.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/command/ShipmentCreateCommand.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/command/ShipmentDeliveryCommand.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/command/ShipmentReturnCommand.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/secondary/ShipmentRepository.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/secondary/ShipmentConfigurationPort.java"))
                .exists();
    }

    @Test
    void shipmentPortShouldOwnFormerShipmentServiceOperationsAsPublicPortMethods() throws IOException {
        final String shipmentPortSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPortImpl.java"));

        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/service/ShipmentServiceImpl.java"))
                .doesNotExist();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/domain/service/ShipmentService.java"))
                .doesNotExist();
        assertThat(shipmentPortSource)
                .doesNotContain("private void notifyRelatedShipmentRedirected")
                .doesNotContain("public void notifyRelatedShipmentRedirected")
                .doesNotContain("public void notifyShipmentRerouted")
                .doesNotContain("public void notifyShipmentSent")
                .doesNotContain("public void notifyShipmentReturned")
                .doesNotContain("public void notifyShipmentDelivered")
                .doesNotContain("public void notifyReturnCanceled")
                .doesNotContain("ApplicationEventPublisher")
                .doesNotContain("nextShipmentId()")
                .contains("ShipmentId.nextId()")
                .doesNotContain("shipmentIdGenerator")
                .contains("shipmentStatusChangeStrategyResolver.resolve(request.shipmentStatus())")
                .contains("shipmentReturnStrategyResolver.resolve(command.getReturnStatus())")
                .doesNotContain("switch (returnStatus)")
                .doesNotContain("case REDIRECT");

        final String redirectedStatusStrategy = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/service/status/ShipmentRedirectedStatusChangeStrategy.java"));
        assertThat(redirectedStatusStrategy)
                .contains("ShipmentStatus.REDIRECT")
                .contains("new ShipmentRedirected(");

        final String shipmentPortContract = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPort.java"));
        assertThat(shipmentPortContract).doesNotContain("void notify");

        final Path shipmentIdGeneratorPort = Path.of(
                "src/main/java/com/warehouse/shipment/application/port/secondary/ShipmentIdGenerator.java");
        final Path shipmentIdGeneratorAdapter = Path.of(
                "src/main/java/com/warehouse/shipment/infrastructure/adapter/secondary/ShipmentIdGeneratorAdapter.java");
        assertThat(shipmentIdGeneratorPort).doesNotExist();
        assertThat(shipmentIdGeneratorAdapter).doesNotExist();
    }

    @Test
    void shipmentCreatedHandlerShouldPublishConcreteIntegrationEvent() throws IOException {
        final String listenerSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/listener/ShipmentEventListener.java"));

        assertThat(listenerSource)
                .contains("handle(final ShipmentCreated event)")
                .contains("new ShipmentCreatedIntegrationEvent(")
                .contains("ShipmentEventData.from(snapshot)")
                .contains("IntegrationEventPublisher")
                .doesNotContain("Optional<")
                .doesNotContain("TODO")
                .doesNotContain("handle(final ShipmentEvent event)");

        final String integrationEvent = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/ShipmentChangedIntegrationEvent.java"));
        assertThat(integrationEvent)
                .contains("@IntegrationEventType(value = \"shipment.changed\", version = 1)")
                .contains("ShipmentChangedEventPayload payload")
                .doesNotContain("UUID eventId")
                .doesNotContain("Instant occurredAt")
                .doesNotContain("private String eventType")
                .doesNotContain("int version");

        final String changedEventPayload = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/ShipmentChangedEventPayload.java"));
        assertThat(changedEventPayload)
                .contains("ShipmentId shipmentId")
                .contains("String eventType")
                .contains("ShipmentStatus shipmentStatus")
                .contains("LocalDateTime changedAt")
                .contains("OperatorId operatorId")
                .contains("DepartmentId departmentId")
                .contains("UserId userId");

        final String integrationSnapshot = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/snapshot/ShipmentEventData.java"));
        assertThat(integrationSnapshot)
                .contains("ShipmentId shipmentId")
                .contains("DepartmentId originDepartmentId")
                .contains("ShipmentId shipmentRelatedId")
                .contains("TrackingNumber trackingNumber")
                .contains("ExternalId<UUID> externalShipmentId")
                .doesNotContain("Long shipmentId")
                .doesNotContain("record SenderSnapshot(")
                .doesNotContain("record RecipientSnapshot(")
                .doesNotContain("record DangerousGoodSnapshot(")
                .doesNotContain("record SignatureSnapshot(");
        final Path snapshotPackage = Path.of(
                "src/main/java/com/warehouse/shipment/application/event/snapshot");
        assertThat(snapshotPackage.resolve("SenderSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("RecipientSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("MoneySnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("DangerousGoodSnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("SignatureSnapshot.java")).exists();
    }

    @Test
    void servicesUsingSecondaryPortsShouldBelongToApplicationLayer() throws IOException {
        final List<String> applicationServices = List.of(
                "CountryDetermineService",
                "CountryServiceAvailabilityService",
                "PriceService",
                "RouteLogService",
                "ShipmentReadModelSyncService",
                "SignatureService"
        );

        applicationServices.forEach(service -> {
            assertThat(Path.of("src/main/java/com/warehouse/shipment/application/service/" + service + ".java"))
                    .exists();
            assertThat(Path.of("src/main/java/com/warehouse/shipment/domain/service/" + service + ".java"))
                    .doesNotExist();
        });

        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/domain/service/TrackingNumberService.java"))
                .exists();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/domain/service/TrackingNumberServiceImpl.java"))
                .exists();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/service/TrackingNumberSequenceService.java"))
                .exists();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/service/TrackingNumberGenerationService.java"))
                .exists();

        final String trackingNumberService = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/domain/service/TrackingNumberServiceImpl.java"));
        assertThat(trackingNumberService)
                .doesNotContain("application.port")
                .doesNotContain("org.springframework")
                .doesNotContain("TrackingSequenceRepository");

        final String shipmentPort = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPortImpl.java"));
        assertThat(shipmentPort)
                .contains("trackingNumberGenerationService.generate(")
                .doesNotContain("TrackingNumberSource")
                .doesNotContain("sequenceValue");
    }

    private java.util.stream.Stream<String> lines(final Path sourceFile) {
        try {
            return Files.lines(sourceFile);
        } catch (final IOException exception) {
            throw new IllegalStateException("Cannot read source file: " + sourceFile, exception);
        }
    }
}
