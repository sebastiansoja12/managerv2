package com.warehouse.shipment;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.eventoutbox.infrastructure.adapter.secondary.SpringDomainEventPublisher;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.context.OperatorAwareEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentTopics;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.IntegrationEventOutboxAdapter;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.ShipmentReadModelChanged;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.application.listener.ShipmentIntegrationEventTranslator;
import com.warehouse.shipment.application.listener.ShipmentReadModelSyncIntegrationEventTranslator;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.service.ShipmentReadModelSyncServiceImpl;
import com.warehouse.shipment.infrastructure.adapter.primary.kafka.ShipmentIntegrationListener;
import com.warehouse.shipment.domain.event.ShipmentChanged;
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

    private static final Path EVENT_OUTBOX_ROOT = Path.of("../../EventOutbox/src/main/java/com/warehouse/eventoutbox");

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
    void shipmentReadOperationsShouldNotExposeAggregateToPrimaryAdapters() throws IOException {
        final String shipmentPortSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPort.java"));
        final String shipmentPortImplementationSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPortImpl.java"));
        final String shipmentControllerSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/ShipmentInternalController.java"));

        assertThat(shipmentPortSource)
                .contains("ShipmentResult loadShipment(final ShipmentId shipmentId)")
                .contains("ShipmentRouteLog loadShipmentWithRouteLog")
                .doesNotContain("Shipment loadShipment")
                .doesNotContain("List<Shipment> searchShipments");
        final String shipmentResultFactorySource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/service/ShipmentResultFactory.java"));
        assertThat(shipmentResultFactorySource)
                .doesNotContain("getTargetDepartmentId")
                .doesNotContain("targetDepartmentId == null");
        assertThat(shipmentPortImplementationSource)
                .contains("shipmentResultFactory.create")
                .contains("shipmentResultFactory.createControlCenter")
                .doesNotContain("resolveDepartmentCode");
        assertThat(shipmentControllerSource)
                .doesNotContain("import com.warehouse.shipment.domain.model.Shipment;")
                .doesNotContain("final Shipment shipment =");
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
                .doesNotExist();
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
                .contains("public void notifyShipmentReturnCompleted")
                .contains("public void notifyShipmentReturnCanceled")
                .doesNotContain("ApplicationEventPublisher")
                .doesNotContain("ShipmentEventContext")
                .contains("DomainEventPublisher")
                .doesNotContain("nextShipmentId()")
                .contains("ShipmentId.nextId()")
                .doesNotContain("shipmentIdGenerator")
                .contains(".resolve(request.shipmentStatus())")
                .doesNotContain("processShipmentReturn")
                .doesNotContain("shipmentReturnStrategyResolver")
                .doesNotContain("switch (returnStatus)")
                .doesNotContain("case REDIRECT");

        final String redirectedStatusStrategy = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/service/status/ShipmentRedirectedStatusChangeStrategy.java"));
        assertThat(redirectedStatusStrategy)
                .contains("ShipmentStatus.REDIRECT")
                .contains("new ShipmentRedirected(");

        final String shipmentPortContract = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/primary/ShipmentPort.java"));
        assertThat(shipmentPortContract)
                .contains("void notifyShipmentReturnCompleted")
                .contains("void notifyShipmentReturnCanceled");

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
        final String translatorSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/listener/ShipmentIntegrationEventTranslator.java"));

        assertThat(translatorSource)
                .contains("translate(final ShipmentCreated event)")
                .contains("@IntegrationEventTranslation")
                .contains("ShipmentEventDataMapperPort")
                .doesNotContain("IntegrationEventPublisher")
                .doesNotContain("@EventListener")
                .doesNotContain("ConditionalOnProperty")
                .doesNotContain("ShipmentPort")
                .doesNotContain("PathFinderServicePort");
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/application/port/secondary/ShipmentIntegrationEventServicePort.java"))
                .doesNotExist();
        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/infrastructure/adapter/secondary/ShipmentIntegrationEventServiceAdapter.java"))
                .doesNotExist();

        final String domainListenerSource = Files.readString(Path.of(
                "src/main/java/com/warehouse/shipment/application/listener/ShipmentDomainEventListener.java"));
        assertThat(domainListenerSource)
                .contains("ShipmentPort")
                .doesNotContain("PathFinderServicePort")
                .doesNotContain("IntegrationEventPublisher")
                .doesNotContain("IntegrationEvent");

        final String integrationEvent = Files.readString(Path.of(
                "../ShipmentManagementApi/src/main/java/com/warehouse/shipment/api/event/ShipmentChangedIntegrationEvent.java"));
        assertThat(integrationEvent)
                .contains("@IntegrationEventType(value = \"shipment.changed\", version = 1, topic = ShipmentTopics.EVENTS)")
                .contains("ShipmentEventData payload")
                .contains("ShipmentChangedIntegrationEvent(@JsonProperty(\"payload\") final ShipmentEventData shipmentEventData)")
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
                "../ShipmentManagementApi/src/main/java/com/warehouse/shipment/api/event/snapshot/ShipmentEventData.java"));
        assertThat(integrationSnapshot)
                .contains("ShipmentId shipmentId")
                .doesNotContain("DepartmentId originDepartmentId")
                .doesNotContain("PickupPointId deliveryPickupPointId")
                .contains("ShipmentId shipmentRelatedId")
                .contains("TrackingNumber trackingNumber")
                .contains("ExternalId<UUID> externalShipmentId")
                .doesNotContain("Long shipmentId")
                .doesNotContain("record SenderSnapshot(")
                .doesNotContain("record RecipientSnapshot(")
                .doesNotContain("record SignatureSnapshot(");
        final Path snapshotPackage = Path.of(
                "../ShipmentManagementApi/src/main/java/com/warehouse/shipment/api/event/snapshot");
        assertThat(snapshotPackage.resolve("PartySnapshot.java")).exists();
        assertThat(snapshotPackage.resolve("MoneySnapshot.java")).exists();
    }

    @Test
    void integrationEventsShouldCarryOperatorContext() {
        final List<Class<?>> integrationEvents = List.of(
                ShipmentChangedIntegrationEvent.class,
                ShipmentCreatedIntegrationEvent.class,
                ShipmentDestinationChangedIntegrationEvent.class,
                ShipmentStatusChangedIntegrationEvent.class,
                ShipmentReadModelChanged.class,
                ShipmentPalletReadModelChanged.class,
                ShipmentCanceledIntegrationEvent.class
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
                ShipmentStatusChangedIntegrationEvent.class,
                ShipmentReadModelChanged.class,
                ShipmentPalletReadModelChanged.class,
                ShipmentCanceledIntegrationEvent.class
        );

        integrationEvents.forEach(eventType ->
                assertThat(IntegrationEventKey.class).isAssignableFrom(eventType));
    }

    @Test
    void integrationEventContractsShouldBeIndependentFromDomainAndKafka() throws IOException {
        final Path commonEventRoot = Path.of("../../Common/src/main/java/com/warehouse/commonassets/event");
        final Path integrationRoot = EVENT_OUTBOX_ROOT.resolve("integration");

        assertThat(integrationRoot.resolve("annotation/IntegrationEventType.java")).exists();
        assertThat(integrationRoot.resolve("model/IntegrationEvent.java")).exists();
        assertThat(integrationRoot.resolve("model/IntegrationEventKey.java")).exists();
        assertThat(commonEventRoot.resolve("integration/context/OperatorAwareEvent.java")).exists();
        assertThat(commonEventRoot.resolve("integration/context/OperatorAwareContext.java")).exists();
        assertThat(commonEventRoot.resolve("domain/annotation/IntegrationEventType.java")).doesNotExist();
        assertThat(commonEventRoot.resolve("domain/model/IntegrationEvent.java")).doesNotExist();
        assertThat(Path.of(
                "../../Common/src/main/java/com/warehouse/commonassets/kafka/domain/model/OperatorAwareEvent.java"))
                .doesNotExist();
        assertThat(Path.of(
                "../../Common/src/main/java/com/warehouse/commonassets/kafka/domain/model/OperatorAwareContext.java"))
                .doesNotExist();

        try (java.util.stream.Stream<Path> sourceFiles = Files.walk(EVENT_OUTBOX_ROOT)) {
            final List<String> managerImports = sourceFiles
                    .filter(path -> path.toString().endsWith(".java"))
                    .flatMap(this::lines)
                    .filter(line -> line.startsWith("import com.warehouse.") && !line.startsWith("import com.warehouse.eventoutbox."))
                    .toList();

            assertThat(managerImports).isEmpty();
        }
    }

    @Test
    void readModelIntegrationEventShouldUseTransportNeutralContract() throws IOException {
        assertThat(IntegrationEventKey.class).isAssignableFrom(ShipmentReadModelChanged.class);

        final String eventSource = Files.readString(Path.of(
                "../ShipmentManagementApi/src/main/java/com/warehouse/shipment/api/event/ShipmentReadModelChanged.java"));
        assertThat(eventSource)
                .contains("ShipmentReadModelData")
                .contains("IntegrationEventKey")
                .doesNotContain("ShipmentSnapshot")
                .doesNotContain("commonassets.kafka");

        final String dataSource = Files.readString(Path.of(
                "../ShipmentManagementApi/src/main/java/com/warehouse/shipment/api/event/snapshot/ShipmentReadModelData.java"));
        assertThat(dataSource)
                .contains("ShipmentId shipmentId")
                .doesNotContain("shipment.domain")
                .doesNotContain("commonassets.kafka");

        assertThat(Path.of(
                "../../Common/src/main/java/com/warehouse/commonassets/kafka/domain/model/KafkaEventKey.java"))
                .doesNotExist();
    }

    @Test
    void palletReadModelIntegrationEventShouldUseTransportNeutralContract() throws IOException {
        final String eventSource = Files.readString(Path.of("../ShipmentManagementApi/src/main/java/com/warehouse/"
                + "shipment/api/event/ShipmentPalletReadModelChanged.java"));
        assertThat(eventSource)
                .contains("ShipmentPalletReadModelData")
                .contains("IntegrationEventKey")
                .doesNotContain("palletmanagement")
                .doesNotContain("commonassets.kafka");

        final String dataSource = Files.readString(Path.of("../ShipmentManagementApi/src/main/java/com/warehouse/"
                + "shipment/api/event/snapshot/ShipmentPalletReadModelData.java"));
        assertThat(dataSource)
                .contains("ShipmentId shipmentId")
                .contains("PalletTrackingNumber palletTrackingNumber")
                .doesNotContain("shipment.domain")
                .doesNotContain("palletmanagement")
                .doesNotContain("commonassets.kafka");

        assertThat(Files.readString(Path.of("src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/"
                + "kafka/ShipmentIntegrationListener.java")))
                .contains("ShipmentPalletReadModelChanged")
                .contains("syncPalletTrackingNumber")
                .doesNotContain("ShipmentReadModelRepository");

        assertThat(ShipmentPalletReadModelChanged.class.getAnnotation(IntegrationEventType.class).topic())
                .isEqualTo(ShipmentTopics.PALLET_READ_MODEL_SYNC);
    }

    @Test
    void readModelInboundAdaptersShouldUsePrimaryPort() throws IOException {
        final List<Path> inboundAdapters = List.of(
                Path.of("src/main/java/com/warehouse/shipment/infrastructure/adapter/primary/kafka/"
                        + "ShipmentIntegrationListener.java"),
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
        assertThat(EVENT_OUTBOX_ROOT.resolve("application/port/secondary/DomainEventPublisher.java")).exists();
        assertThat(EVENT_OUTBOX_ROOT.resolve(
                "infrastructure/adapter/secondary/SpringDomainEventPublisher.java")).exists();
    }

    @Test
    void kafkaComponentsShouldUseDedicatedFeatureFlags() throws IOException, NoSuchMethodException {
        assertThat(ShipmentIntegrationEventTranslator.class.getAnnotation(ConditionalOnProperty.class)).isNull();
        assertThat(ShipmentReadModelSyncIntegrationEventTranslator.class.getAnnotation(ConditionalOnProperty.class)).isNull();
        assertThat(ShipmentIntegrationListener.class.getAnnotation(ConditionalOnProperty.class)).isNull();

        assertThat(Path.of(
                "src/main/java/com/warehouse/shipment/infrastructure/adapter/secondary/kafka/ShipmentReadModelSyncKafkaPublisher.java"))
                .doesNotExist();
        assertThat(Path.of(
                "../ShipmentManagementApi/src/main/java/com/warehouse/shipment/api/event/ShipmentReadModelChanged.java"))
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
        assertThat(EVENT_OUTBOX_ROOT.resolve("application/port/secondary/IntegrationEventPublisher.java")).exists();

        final String properties = Files.readString(Path.of("../../Application/src/main/resources/application.properties"));
        assertThat(properties)
                .contains("manager.events.outbox.enabled=")
                .doesNotContain("manager.kafka.integration-events.enabled=")
                .doesNotContain("manager.kafka.shipment-read-model-sync.enabled=")
                .contains("manager.kafka.consumer.retry.max-attempts=")
                .contains("manager.kafka.consumer.retry.backoff-ms=")
                .contains("manager.kafka.consumer.dlt-suffix=")
                .contains("manager.kafka.consumer.dlt.publish-timeout-ms=")
                .doesNotContain("manager.kafka.shipment-read-model-sync.retry.")
                .doesNotContain("manager.kafka.shipment-read-model-sync.dlt-")
                .doesNotContain("manager.kafka.integration-events.routes.")
                .doesNotContain("manager.kafka.domain-events.enabled=");

        assertThat(OutboxEventStatus.values()).containsExactly(
                OutboxEventStatus.PENDING,
                OutboxEventStatus.PUBLISHED,
                OutboxEventStatus.DEAD);
        final String outboxMigration = Files.readString(Path.of(
                "../../Application/src/main/resources/changelog/db/postgresql/changes/2026-11-18-integration-event-outbox.xml"));
        assertThat(outboxMigration)
                .contains("name=\"sequence_number\"")
                .contains("name=\"status\"")
                .contains("name=\"attempt_count\"")
                .contains("name=\"next_attempt_at\"")
                .doesNotContain("name=\"locked_by\"");

        assertThat(transactionPropagation(IntegrationEventOutboxAdapter.class, "publish", IntegrationEvent.class))
                .isEqualTo(Propagation.MANDATORY);
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
                "DepartmentCountryAvailabilityService",
                "PriceService",
                "RouteLogService"
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
