package com.warehouse.shipment;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.commonassets.event.infrastructure.adapter.secondary.SpringDomainEventPublisher;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.context.OperatorAwareEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.kafka.application.KafkaOutboxPublicationService;
import com.warehouse.commonassets.kafka.application.TransactionalKafkaOutboxWriter;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxStatus;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.JdbcKafkaOutboxAdapter;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.OutboxIntegrationEventPublisher;
import com.warehouse.shipment.application.event.ShipmentCanceledMessage;
import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCanceledIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReadModelChanged;
import com.warehouse.shipment.application.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.application.listener.ShipmentIntegrationEventListener;
import com.warehouse.shipment.application.listener.ShipmentReadModelSyncIntegrationEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.service.ShipmentReadModelSyncServiceImpl;
import com.warehouse.shipment.infrastructure.adapter.primary.kafka.ShipmentReadModelSyncListener;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import com.warehouse.shipment.domain.event.SignatureChangedEvent;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        assertThat(ShipmentReadModelSyncPort.class).isAssignableFrom(ShipmentReadModelSyncServiceImpl.class);
    }

    @Test
    void shipmentStatusChangeShouldBeTransactional() throws NoSuchMethodException {
        assertThat(transactionPropagation(
                ShipmentPortImpl.class,
                "changeShipmentStatusTo",
                ShipmentStatusRequest.class))
                .isEqualTo(Propagation.REQUIRED);
    }

    @Test
    void shipmentPortsShouldBelongToApplicationLayer() {
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPort.java"))
                .exists();
        assertThat(Path.of("src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPortImpl.java"))
                .exists();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentReadModelSyncPort.java"))
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
                .doesNotContain("ShipmentEventContext")
                .contains("DomainEventPublisher")
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
        assertThat(Path.of("src/main/java/com/warehouse/shipment/domain/context/ShipmentEventContext.java"))
                .doesNotExist();
    }

    @Test
    void integrationEventListenerShouldOnlyTranslateDomainEvents() throws IOException {
        final String listenerSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/listener/ShipmentIntegrationEventListener.java"));

        assertThat(listenerSource)
                .contains("handle(final ShipmentCreated event)")
                .contains("new ShipmentCreatedIntegrationEvent(")
                .contains("ShipmentEventData.from(event.getSnapshot())")
                .contains("IntegrationEventPublisher")
                .doesNotContain("ShipmentPort")
                .doesNotContain("PathFinderServicePort")
                .doesNotContain("Optional<")
                .doesNotContain("TODO")
                .doesNotContain("handle(final ShipmentEvent event)");

        final String domainListenerSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/listener/ShipmentDomainEventListener.java"));
        assertThat(domainListenerSource)
                .contains("ShipmentPort")
                .contains("PathFinderServicePort")
                .doesNotContain("IntegrationEventPublisher")
                .doesNotContain("IntegrationEvent");

        final String integrationEvent = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/ShipmentChangedIntegrationEvent.java"));
        assertThat(integrationEvent)
                .contains("@IntegrationEventType(value = \"shipment.changed\", version = 1)")
                .contains("ShipmentEventData payload")
                .contains("ShipmentChangedIntegrationEvent(final ShipmentEventData shipmentEventData)")
                .doesNotContain("UUID eventId")
                .doesNotContain("Instant occurredAt")
                .doesNotContain("private String eventType")
                .doesNotContain("int version");
        assertThat(ShipmentChangedIntegrationEvent.class.getDeclaredConstructors()).hasSize(1);
        assertThat(ShipmentChangedIntegrationEvent.class.getDeclaredConstructors()[0].getParameterTypes())
                .containsExactly(ShipmentEventData.class);

        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/ShipmentChangedEventPayload.java"))
                .doesNotExist();

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
    void integrationEventsShouldCarryOperatorContext() {
        final List<Class<?>> integrationEvents = List.of(
                ShipmentChangedIntegrationEvent.class,
                ShipmentCreatedIntegrationEvent.class,
                ShipmentDestinationChangedIntegrationEvent.class,
                ShipmentReturnCanceledIntegrationEvent.class,
                ShipmentReturnCreatedIntegrationEvent.class,
                ShipmentStatusChangedIntegrationEvent.class,
                ShipmentReadModelChanged.class,
                ShipmentCanceledMessage.class
        );

        integrationEvents.forEach(eventType ->
                assertThat(OperatorAwareContext.class).isAssignableFrom(eventType));
    }

    @Test
    void shipmentIntegrationEventsShouldDefineKafkaMessageKey() {
        final List<Class<?>> integrationEvents = List.of(
                ShipmentChangedIntegrationEvent.class,
                ShipmentCreatedIntegrationEvent.class,
                ShipmentDestinationChangedIntegrationEvent.class,
                ShipmentReturnCanceledIntegrationEvent.class,
                ShipmentReturnCreatedIntegrationEvent.class,
                ShipmentStatusChangedIntegrationEvent.class,
                ShipmentReadModelChanged.class,
                ShipmentCanceledMessage.class
        );

        integrationEvents.forEach(eventType ->
                assertThat(IntegrationEventKey.class).isAssignableFrom(eventType));
    }

    @Test
    void integrationEventContractsShouldBeIndependentFromDomainAndKafka() throws IOException {
        final Path commonEventRoot = Path.of("../../Common/src/main/java/com/warehouse/commonassets/event");
        final Path integrationRoot = commonEventRoot.resolve("integration");

        assertThat(integrationRoot.resolve("annotation/IntegrationEventType.java")).exists();
        assertThat(integrationRoot.resolve("model/IntegrationEvent.java")).exists();
        assertThat(integrationRoot.resolve("model/IntegrationEventKey.java")).exists();
        assertThat(integrationRoot.resolve("context/OperatorAwareEvent.java")).exists();
        assertThat(integrationRoot.resolve("context/OperatorAwareContext.java")).exists();
        assertThat(commonEventRoot.resolve("domain/annotation/IntegrationEventType.java")).doesNotExist();
        assertThat(commonEventRoot.resolve("domain/model/IntegrationEvent.java")).doesNotExist();
        assertThat(Path.of(
                "../../Common/src/main/java/com/warehouse/commonassets/kafka/domain/model/OperatorAwareEvent.java"))
                .doesNotExist();
        assertThat(Path.of(
                "../../Common/src/main/java/com/warehouse/commonassets/kafka/domain/model/OperatorAwareContext.java"))
                .doesNotExist();

        try (java.util.stream.Stream<Path> sourceFiles = Files.walk(integrationRoot)) {
            final List<String> kafkaImports = sourceFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .flatMap(this::lines)
                    .filter(line -> line.startsWith("import com.warehouse.commonassets.kafka"))
                    .toList();

            assertThat(kafkaImports).isEmpty();
        }
    }

    @Test
    void readModelIntegrationEventShouldUseTransportNeutralContract() throws IOException {
        assertThat(IntegrationEventKey.class).isAssignableFrom(ShipmentReadModelChanged.class);

        final String eventSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/ShipmentReadModelChanged.java"));
        assertThat(eventSource)
                .contains("ShipmentReadModelData")
                .contains("IntegrationEventKey")
                .doesNotContain("ShipmentSnapshot")
                .doesNotContain("commonassets.kafka");

        final String dataSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/snapshot/ShipmentReadModelData.java"));
        assertThat(dataSource)
                .contains("ShipmentId shipmentId")
                .doesNotContain("shipment.domain")
                .doesNotContain("commonassets.kafka");

        assertThat(Path.of(
                "../../Common/src/main/java/com/warehouse/commonassets/kafka/domain/model/KafkaEventKey.java"))
                .doesNotExist();
    }

    @Test
    void readModelInboundAdaptersShouldUsePrimaryPort() throws IOException {
        final List<Path> inboundAdapters = List.of(
                Path.of("src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/kafka/"
                        + "ShipmentReadModelSyncListener.java"),
                Path.of("src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/"
                        + "ShipmentReadSyncController.java"),
                Path.of("src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/"
                        + "ShipmentReadModelRebuildApiServiceAdapter.java")
        );

        for (final Path adapter : inboundAdapters) {
            assertThat(Files.readString(adapter))
                    .contains("ShipmentReadModelSyncPort")
                    .doesNotContain("ShipmentReadModelSyncService");
        }

        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/service/ShipmentReadModelSyncService.java"))
                .doesNotExist();
    }

    @Test
    void domainEventsShouldNotCarryOperatorContextOrDependOnKafka() throws IOException {
        assertThat(OperatorAwareEvent.class.isAssignableFrom(DomainEvent.class)).isFalse();
        assertThat(OperatorAwareContext.class.isAssignableFrom(ShipmentChanged.class)).isFalse();
        assertThat(OperatorAwareContext.class.isAssignableFrom(SignatureChangedEvent.class)).isFalse();

        final Path domainEventsRoot = Path.of("src/main/java/com/warehouse/shipment/domain/event");
        try (java.util.stream.Stream<Path> sourceFiles = Files.walk(domainEventsRoot)) {
            final List<String> kafkaImports = sourceFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .flatMap(this::lines)
                    .filter(line -> line.startsWith("import com.warehouse.commonassets.kafka"))
                    .toList();

            assertThat(kafkaImports).isEmpty();
        }
    }

    @Test
    void domainEventPublisherShouldBeSecondaryPortWithSpringAdapter() {
        assertThat(DomainEventPublisher.class).isAssignableFrom(SpringDomainEventPublisher.class);

        final Path commonEventRoot = Path.of("../../Common/src/main/java/com/warehouse/commonassets/event");
        assertThat(commonEventRoot.resolve("domain/port/DomainEventPublisher.java")).doesNotExist();
        assertThat(commonEventRoot.resolve("application/SpringDomainEventPublisher.java")).doesNotExist();
        assertThat(commonEventRoot.resolve("application/port/secondary/DomainEventPublisher.java")).exists();
        assertThat(commonEventRoot.resolve(
                "infrastructure/adapter/secondary/SpringDomainEventPublisher.java")).exists();
    }

    @Test
    void kafkaComponentsShouldUseDedicatedFeatureFlags() throws IOException, NoSuchMethodException {
        assertThat(propertyNames(OutboxIntegrationEventPublisher.class))
                .containsExactly("manager.kafka.outbox.enabled");
        assertThat(propertyNames(KafkaOutboxPublicationService.class))
                .containsExactly("manager.kafka.outbox.enabled");
        assertThat(propertyNames(TransactionalKafkaOutboxWriter.class))
                .containsExactly("manager.kafka.outbox.enabled");
        assertThat(propertyNames(JdbcKafkaOutboxAdapter.class))
                .containsExactly("manager.kafka.outbox.enabled");
        assertThat(propertyNames(ShipmentIntegrationEventListener.class))
                .containsExactly("manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled");
        assertThat(propertyNames(ShipmentReadModelSyncIntegrationEventListener.class))
                .containsExactly("manager.kafka.shipment-read-model-sync.enabled", "manager.kafka.outbox.enabled");
        assertThat(propertyNames(ShipmentReadModelSyncListener.class))
                .containsExactly("manager.kafka.shipment-read-model-sync.enabled");

        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/infrastructure/adapter/secondary/kafka/ShipmentReadModelSyncKafkaPublisher.java"))
                .doesNotExist();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/event/ShipmentReadModelChanged.java"))
                .exists();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/kafka/ShipmentReadModelKafkaConfiguration.java"))
                .doesNotExist();

        final Path commonKafkaRoot = Path.of("../../Common/src/main/java/com/warehouse/commonassets/kafka");
        assertThat(commonKafkaRoot.resolve("application/KafkaDomainEventExternalizer.java")).doesNotExist();
        assertThat(commonKafkaRoot.resolve("application/IntegrationEventOutboxListener.java")).doesNotExist();
        assertThat(commonKafkaRoot.resolve("domain/annotation/KafkaDomainEvent.java")).doesNotExist();
        assertThat(commonKafkaRoot.resolve("domain/annotation/KafkaDomainEvents.java")).doesNotExist();

        final Path commonEventRoot = Path.of("../../Common/src/main/java/com/warehouse/commonassets/event");
        assertThat(commonEventRoot.resolve("application/SpringIntegrationEventPublisher.java")).doesNotExist();
        assertThat(commonEventRoot.resolve("domain/port/IntegrationEventPublisher.java")).doesNotExist();
        assertThat(commonEventRoot.resolve("application/port/secondary/IntegrationEventPublisher.java")).exists();

        final String properties = Files.readString(Path.of("../../Application/src/main/resources/application.properties"));
        assertThat(properties)
                .contains("manager.kafka.outbox.enabled=")
                .contains("manager.kafka.integration-events.enabled=")
                .contains("manager.kafka.shipment-read-model-sync.enabled=")
                .contains("manager.kafka.consumer.retry.max-attempts=")
                .contains("manager.kafka.consumer.retry.backoff-ms=")
                .contains("manager.kafka.consumer.dlt-suffix=")
                .contains("manager.kafka.consumer.dlt.publish-timeout-ms=")
                .doesNotContain("manager.kafka.shipment-read-model-sync.retry.")
                .doesNotContain("manager.kafka.shipment-read-model-sync.dlt-")
                .contains("manager.kafka.integration-events.routes.shipment.status.changed="
                        + "${manager.kafka.topics.shipment-events}")
                .contains("manager.kafka.integration-events.routes.shipment.read-model.changed=")
                .doesNotContain("manager.kafka.domain-events.enabled=");

        assertThat(KafkaOutboxStatus.values()).containsExactly(
                KafkaOutboxStatus.PENDING,
                KafkaOutboxStatus.PROCESSING,
                KafkaOutboxStatus.PUBLISHED,
                KafkaOutboxStatus.DEAD);
        final String outboxMigration = Files.readString(Path.of(
                "../../Application/src/main/resources/changelog/db/kafka_event_outbox.xml"));
        assertThat(outboxMigration)
                .contains("name=\"status\"")
                .contains("name=\"attempt_count\"")
                .contains("name=\"next_attempt_at\"")
                .contains("name=\"locked_by\"")
                .contains("name=\"locked_until\"");

        assertThat(transactionPropagation(JdbcKafkaOutboxAdapter.class, "save", KafkaOutboxRecord.class))
                .isEqualTo(Propagation.REQUIRED);
        assertThat(transactionPropagation(
                JdbcKafkaOutboxAdapter.class,
                "claimPending",
                int.class,
                String.class,
                java.time.Instant.class,
                java.time.Instant.class))
                .isEqualTo(Propagation.REQUIRES_NEW);
        assertThat(transactionPropagation(
                JdbcKafkaOutboxAdapter.class,
                "claim",
                java.util.UUID.class,
                String.class,
                java.time.Instant.class,
                java.time.Instant.class))
                .isEqualTo(Propagation.REQUIRES_NEW);
        assertThat(transactionPropagation(
                JdbcKafkaOutboxAdapter.class,
                "markPublished",
                java.util.UUID.class,
                String.class))
                .isEqualTo(Propagation.REQUIRES_NEW);
        assertThat(transactionPropagation(
                JdbcKafkaOutboxAdapter.class,
                "markFailed",
                java.util.UUID.class,
                String.class,
                Throwable.class,
                java.time.Instant.class,
                int.class))
                .isEqualTo(Propagation.REQUIRES_NEW);
    }

    private String[] propertyNames(final Class<?> componentType) {
        return componentType.getAnnotation(ConditionalOnProperty.class).name();
    }

    private Propagation transactionPropagation(final Class<?> componentType,
                                                final String methodName,
                                                final Class<?>... parameterTypes) throws NoSuchMethodException {
        return componentType.getDeclaredMethod(methodName, parameterTypes)
                .getAnnotation(Transactional.class)
                .propagation();
    }

    @Test
    void servicesUsingSecondaryPortsShouldBelongToApplicationLayer() throws IOException {
        final List<String> applicationServices = List.of(
                "CountryDetermineService",
                "CountryServiceAvailabilityService",
                "PriceService",
                "RouteLogService",
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
