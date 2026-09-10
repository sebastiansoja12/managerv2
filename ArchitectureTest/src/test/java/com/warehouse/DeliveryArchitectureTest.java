package com.warehouse;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DeliveryArchitectureTest {

    private static final String DELIVERY = "com.warehouse.logisticsdelivery";

    private static final JavaClasses DELIVERY_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(DELIVERY);

    private static final JavaClasses LOGISTICS_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.warehouse.logistics");

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage(DELIVERY + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY + ".application..", DELIVERY + ".infrastructure..", DELIVERY + ".configuration..",
                        DELIVERY + ".api..", "org.springframework..", "jakarta.persistence..", "lombok..",
                        "com.fasterxml.jackson..", "org.slf4j..")
                .check(DELIVERY_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdapters() {
        noClasses().that().resideInAPackage(DELIVERY + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY + ".infrastructure..", DELIVERY + ".configuration..", "jakarta.persistence..")
                .check(DELIVERY_CLASSES);
    }

    @Test
    void shouldConnectPrimaryAdaptersOnlyToPrimaryPorts() {
        noClasses().that().resideInAPackage(DELIVERY + ".infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY + ".infrastructure.adapter.secondary..", DELIVERY + ".application.port.secondary..")
                .check(DELIVERY_CLASSES);
    }

    @Test
    void shouldImplementSecondaryPortsInSecondaryAdapters() {
        classes().that().haveSimpleName("DeliveryRepositoryImpl")
                .should().implement(DELIVERY + ".application.port.secondary.DeliveryRepository")
                .check(DELIVERY_CLASSES);
        classes().that().haveSimpleName("DeliveryReadModelRepositoryImpl")
                .should().implement(DELIVERY + ".application.port.secondary.DeliveryReadModelRepository")
                .check(DELIVERY_CLASSES);
        classes().that().haveSimpleName("DeliveryHistoryRepositoryImpl")
                .should().implement(DELIVERY + ".application.port.secondary.DeliveryHistoryRepository")
                .check(DELIVERY_CLASSES);
        classes().that().haveSimpleName("DeliveryIntegrationEventServiceAdapter")
                .should().implement(DELIVERY + ".application.port.secondary.DeliveryIntegrationEventServicePort")
                .check(DELIVERY_CLASSES);
        classes().that().haveSimpleName("DeliveryEventDataMapper")
                .should().implement(DELIVERY + ".application.port.secondary.DeliveryEventDataMapperPort")
                .check(DELIVERY_CLASSES);
    }

    @Test
    void shouldAccessDeliveryFromOtherLogisticsPackagesOnlyThroughApi() {
        noClasses().that().resideInAPackage("com.warehouse.logistics..")
                .and().resideOutsideOfPackage(DELIVERY + "..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY + ".domain..", DELIVERY + ".application..", DELIVERY + ".infrastructure..",
                        DELIVERY + ".configuration..")
                .check(LOGISTICS_CLASSES);
    }
}
